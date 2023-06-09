package fr.epf.matmob.filmroll

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import fr.epf.matmob.filmroll.model.FavouriteFilm
import fr.epf.matmob.filmroll.model.Film
import fr.epf.matmob.filmroll.model.Genre
import fr.epf.matmob.filmroll.model.Person
import fr.epf.matmob.filmroll.state.FilmApplication
import fr.epf.matmob.filmroll.state.FilmViewModel
import fr.epf.matmob.filmroll.state.FilmViewModelFactory
import fr.epf.matmob.filmroll.ui.components.FilmCarousel
import fr.epf.matmob.filmroll.ui.theme.FilmrollTheme
import java.net.URL
import kotlin.math.roundToInt

private const val TAG = "FilmCardActivity"

class FilmCardActivity : ComponentActivity() {
    private val filmViewModel: FilmViewModel by viewModels {
        FilmViewModelFactory((application as FilmApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filmId = intent.extras?.getInt("TMDBFilmId")
        if (filmId != null) {
            filmViewModel.getFilm(filmId)
            filmViewModel.isFilmFavourite(filmId)
        } else {
            finish()
        }
        setContent {
            FilmrollTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    DisplayFilmCard(viewModel = filmViewModel, filmId)
                }
            }
        }
    }
}

@Composable
fun DisplayFilmCard(viewModel: FilmViewModel, filmId: Int?) {
    val filmInfo by viewModel.filmInfo.observeAsState()
    val filmIsFavourite by viewModel.isFilmFavourite.observeAsState()
    Scaffold(topBar = {
        filmIsFavourite?.let {
            var isFavourite by rememberSaveable { mutableStateOf(it) }
            FilmCardTopBar(
                viewModel = viewModel,
                filmId!!,
                isFavourite,
                onFavouriteStateChange = { newVal -> isFavourite = newVal })
        }
    }) { padValues ->
        LazyColumn(modifier = Modifier.padding(padValues)) {
            item {
                filmInfo?.let {
                    Log.d(TAG, "onCreate: filmInfo: $filmInfo")

                    FilmDetails(film = it.film)
                    Spacer(modifier = Modifier.height(16.dp))
                    PersonList(persons = it.cast, title = "Cast members")
                    Spacer(modifier = Modifier.height(12.dp))
                    PersonList(persons = it.crew, title = "Crew members")
                    Spacer(modifier = Modifier.height(16.dp))
                    FilmCarousel(
                        films = it.recommendations, title = "Recommended films"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmCardTopBar(
    viewModel: FilmViewModel,
    filmId: Int,
    favouriteState: Boolean,
    onFavouriteStateChange: (Boolean) -> Unit
) {
    TopAppBar(title = {
        Text(
            text = "Film Details",
            style = MaterialTheme.typography.headlineMedium
        )
    }, actions = {
        IconButton(onClick = {
            val newFavState = !favouriteState
            onFavouriteStateChange(newFavState)
            viewModel.insert(FavouriteFilm(filmId, newFavState))
        }) {
            if (favouriteState) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "This film is a favourite"
                )
            } else {
                Icon(
                    imageVector = Icons.TwoTone.Star,
                    contentDescription = "This film is not a favourite"
                )
            }
        }
    })
}

@Composable
fun GenrePill(genre: Genre) {
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = genre.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

/**
 * Displays a clickable hyperlink.
 * Heavily inspired from the web.
 */
@Composable
fun Hyperlink(link: URL, modifier: Modifier = Modifier) {
    val annotatedString = buildAnnotatedString {
        val linkLength = link.toString().length
        append(link.toString())
        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.tertiary,
                textDecoration = TextDecoration.Underline
            ), start = 0, end = linkLength
        )
        addStringAnnotation(
            tag = "URL", annotation = link.toString(), start = 0, end = linkLength
        )
    }
    val uriHandler = LocalUriHandler.current
    ClickableText(modifier = modifier, text = annotatedString, onClick = {
        annotatedString.getStringAnnotations("URL", it, it).firstOrNull()?.let { stringAnnotation ->
            uriHandler.openUri(stringAnnotation.item)
        }
    })
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FilmDetails(
    film: Film
) {
    Column {
        GlideImage(
            model = if (film.backdropPath.isEmpty()) {
                "https://placehold.co/1280x720.png"
            } else {
                "https://image.tmdb.org/t/p/w1280${film.backdropPath}"
            },
            contentDescription = "${film.title}'s backdrop image",
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = film.title, style = MaterialTheme.typography.headlineMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            items(film.genres) {
                GenrePill(genre = it)
            }
        }
        Text(
            text = "Publication : ${film.releaseDate}", style = MaterialTheme.typography.bodyMedium
        )
        Hyperlink(link = film.homepage)
        Text(text = film.synopsis, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "Score : ${(film.voteScore * 10).roundToInt()} %",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(text = "${film.duration}", style = MaterialTheme.typography.bodyMedium)

        /*
        - Language
        - Synopsis
        - User rating
        - Age rating
        - Length (duration)
        - Publication year
        */
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PersonProfile(person: Person) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(end = 15.dp)
        ) {
            GlideImage(
                model = if (person.picturePath.isEmpty()) {
                    "https://placehold.co/185x278.png"
                } else {
                    "https://image.tmdb.org/t/p/w185${person.picturePath}"
                },
                contentDescription = "${person.name}'s profile picture",
                modifier = Modifier.width(27.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = person.name, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = person.role, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun PersonList(persons: List<Person>, title: String) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(persons) {
                PersonProfile(person = it)
            }
        }
    }
}

@Preview
@Composable
fun GenrePillPreview() {
    FilmrollTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            GenrePill(genre = Genre(0, "Science Fiction"))
            GenrePill(genre = Genre(0, "Action"))
            GenrePill(genre = Genre(0, "Thriller"))
        }
    }
}

@Preview
@Composable
fun PersonProfilePreview() {
    FilmrollTheme {
        val person = Person(0, "John Doe", "Camera", "")
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            PersonProfile(person = person)
            PersonProfile(person = person)
        }
    }
}