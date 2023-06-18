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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import fr.epf.matmob.filmroll.model.FavouriteFilm
import fr.epf.matmob.filmroll.model.Film
import fr.epf.matmob.filmroll.model.Genre
import fr.epf.matmob.filmroll.model.LiteFilm
import fr.epf.matmob.filmroll.model.Person
import fr.epf.matmob.filmroll.state.FilmApplication
import fr.epf.matmob.filmroll.state.FilmViewModel
import fr.epf.matmob.filmroll.state.FilmViewModelFactory
import fr.epf.matmob.filmroll.ui.components.FilmCarousel
import fr.epf.matmob.filmroll.ui.theme.FilmrollTheme
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
                    filmId?.let { DisplayFilmCard(viewModel = filmViewModel, it) }
                }
            }
        }
    }
}

@Composable
fun DisplayFilmCard(viewModel: FilmViewModel, filmId: Int) {
    val filmInfo by viewModel.filmInfo.observeAsState()
    val filmIsFavourite by viewModel.isFilmFavourite.observeAsState()
    Scaffold(topBar = {
        filmIsFavourite?.let {
            filmInfo?.film?.toLiteFilm()?.let { it1 ->
                FilmCardTopBar(
                    viewModel = viewModel, filmId, it, it1
                )
            }
        }
    }) { padValues ->
        LazyColumn(modifier = Modifier.padding(padValues)) {
            item {
                filmInfo?.let {
                    Log.d(TAG, "onCreate: filmInfo: $filmInfo")

                    FilmDetails(film = it.film)
                    Spacer(modifier = Modifier.height(16.dp))
                    PersonList(
                        persons = it.cast, title = stringResource(R.string.cast_members_list_title)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PersonList(
                        persons = it.crew, title = stringResource(R.string.crew_members_list_title)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FilmCarousel(
                        films = it.recommendations,
                        title = stringResource(R.string.recommended_films_title)
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
    viewModel: FilmViewModel, filmId: Int, favouriteState: Boolean, film: LiteFilm
) {
    var isFavourite by rememberSaveable { mutableStateOf(favouriteState) }
    TopAppBar(title = {
        Text(
            text = stringResource(R.string.film_card_screen_name),
            style = MaterialTheme.typography.headlineSmall
        )
    }, actions = {
        IconButton(onClick = {
            isFavourite = !isFavourite
            viewModel.insertFavourite(FavouriteFilm(filmId, isFavourite))
            viewModel.insertLiteFilm(film)
        }) {
            if (isFavourite) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = stringResource(R.string.is_favourite_icon_description),
                    tint = Color(222, 175, 55)
                )
            } else {
                Icon(
                    imageVector = Icons.TwoTone.Star,
                    contentDescription = stringResource(R.string.is_not_favourite_icon_description)
                )
            }
        }
    })
}

@Composable
fun GenrePill(genre: Genre) {
    Surface(
        shape = MaterialTheme.shapes.extraSmall, color = MaterialTheme.colorScheme.secondary
    ) {
        Text(
            text = genre.name,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

/**
 * Displays a clickable hyperlink.
 * Heavily inspired from the web.
 */
@Composable
fun Hyperlink(link: String, modifier: Modifier = Modifier) {
    if (link.isNotEmpty()) {
        val annotatedString = buildAnnotatedString {
            val linkLength = link.length
            append(link)
            addStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.tertiary,
                    textDecoration = TextDecoration.Underline
                ), start = 0, end = linkLength
            )
            addStringAnnotation(
                tag = "URL", annotation = link, start = 0, end = linkLength
            )
        }
        val uriHandler = LocalUriHandler.current
        ClickableText(modifier = modifier, text = annotatedString, onClick = {
            annotatedString.getStringAnnotations("URL", it, it).first()
                .let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
        })
    }
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
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .fillMaxWidth(),
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(all = 8.dp)) {
                Text(text = film.title, style = MaterialTheme.typography.headlineMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(film.genres) {
                        GenrePill(genre = it)
                    }
                }
                Text(
                    text = stringResource(
                        id = R.string.release_date_label, film.releaseDate
                    ), style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(
                        id = R.string.vote_score_label, (film.voteScore * 10).roundToInt()
                    ), style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = film.synopsis,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify
                )
                Text(
                    text = stringResource(id = R.string.duration_label, film.duration.toString()),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Hyperlink(link = film.homepage)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PersonProfile(person: Person) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.tertiaryContainer
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
    Column(modifier = Modifier.padding(horizontal = 6.dp)) {
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