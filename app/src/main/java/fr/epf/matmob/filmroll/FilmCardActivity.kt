package fr.epf.matmob.filmroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import fr.epf.matmob.filmroll.model.Film
import fr.epf.matmob.filmroll.ui.theme.FilmrollTheme
import java.net.URL
import kotlin.math.roundToInt

private const val TAG = "FilmCardActivity"

class FilmCardActivity : ComponentActivity() {
    private val filmViewModel: FilmViewModel by viewModels() {
        FilmViewModelFactory((application as FilmApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filmId = intent.extras?.getInt("TMDBFilmId")
        if (filmId != null) {
            filmViewModel.getMovie(filmId)
        } else {
            finish()
        }
        setContent {
            FilmrollTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val film by filmViewModel.film.observeAsState()
                    film?.let { FilmDetails(film = it) }
                }
            }
        }
    }
}

@Composable
fun GenrePill(genre: String) {
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = genre,
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
fun FilmDetails(film: Film) {
    Column {
        GlideImage(
            model = if (film.backdrop_path.isEmpty()) {
                "https://placehold.co/1600x900.png"
            } else {
                "https://image.tmdb.org/t/p/w780${film.backdrop_path}"
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
            text = "Publication : ${film.release_date[1]}", style = MaterialTheme.typography.bodyMedium
        )
        Hyperlink(link = film.homepage)
        Text(text = film.synopsis, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "Score : ${(film.vote_score * 10).roundToInt()} %",
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

@Preview
@Composable
fun GenrePillPreview() {
    FilmrollTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            GenrePill(genre = "Science Fiction")
            GenrePill(genre = "Action")
            GenrePill(genre = "Thriller")
        }
    }
}