package fr.epf.matmob.filmroll

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import fr.epf.matmob.filmroll.model.LiteFilm
import fr.epf.matmob.filmroll.state.FilmApplication
import fr.epf.matmob.filmroll.state.FilmViewModel
import fr.epf.matmob.filmroll.state.FilmViewModelFactory
import fr.epf.matmob.filmroll.ui.theme.FilmrollTheme

private const val TAG = "FilmResultsList"

class FilmResultsList : ComponentActivity() {
    private val viewModel: FilmViewModel by viewModels {
        FilmViewModelFactory((application as FilmApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val query = intent.extras?.getString("searchQuery")
        if (query != null) {
            viewModel.searchFilm(query)
        } else {
            finish()
        }
        setContent {
            FilmrollTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val foundFilms by viewModel.foundFilms.observeAsState()
                    Column {
                        Text(text = "Texte cherché : $query")
                        foundFilms?.let { ResultsList(filmList = it) }
                    }
                }
            }
        }
    }
}

@Composable
fun ResultsList(filmList: List<LiteFilm>) {
    LazyColumn {
        items(filmList) {
            FilmResult(film = it)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FilmResult(film: LiteFilm) {
    val activity = LocalContext.current as Activity
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(142.dp)
            .clickable {
                activity.startActivity(
                    Intent(
                        activity,
                        FilmCardActivity::class.java
                    ).putExtra("TMDBFilmId", film.tmdbId)
                )
            },
        shape = MaterialTheme.shapes.large
    ) {
        Row {
            GlideImage(
                model = if (film.posterPath.isEmpty()) {
                    "https://placehold.co/342x513.png"
                } else {
                    "https://image.tmdb.org/t/p/w342${film.posterPath}"
                },
                contentDescription = "${film.title}'s poster image",
                modifier = Modifier.width(96.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = film.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = film.releaseDate,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = film.synopsis,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }
}