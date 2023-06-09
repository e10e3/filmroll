package fr.epf.matmob.filmroll.ui.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import fr.epf.matmob.filmroll.FilmCardActivity
import fr.epf.matmob.filmroll.model.LiteFilm

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FilmListItem(film: LiteFilm) {
    val activity = LocalContext.current as Activity
    Surface(
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.primaryContainer,
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

@Composable
fun VerticalFilmList(filmList: List<LiteFilm>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(filmList) {
            FilmListItem(film = it)
        }
    }
}