package fr.epf.matmob.filmroll.ui.components

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import fr.epf.matmob.filmroll.FilmCardActivity
import fr.epf.matmob.filmroll.model.LiteFilm

@Composable
fun FilmCarousel(films: List<LiteFilm>, title: String) {
    val activity = LocalContext.current as Activity
    val columnWidth = 111.dp
    Surface(
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        modifier = Modifier.padding(horizontal = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp, top = 12.dp, start = 12.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 12.dp, end = 4.dp)
            ) {
                items(films) {
                    PosterItem(film = it, onClick = {
                        activity.startActivity(
                            Intent(
                                activity, FilmCardActivity::class.java
                            ).putExtra("TMDBFilmId", it.tmdbId)
                        )
                    }, modifier = Modifier.width(columnWidth))
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PosterItem(film: LiteFilm, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.clickable {
        onClick()
    }) {
        GlideImage(
            model = if (film.posterPath.isEmpty()) {
                "https://placehold.co/342x513.png"
            } else {
                "https://image.tmdb.org/t/p/w342${film.posterPath}"
            },
            contentDescription = "${film.title}'s poster image",
            modifier = Modifier
                .shadow(elevation = 3.dp)
                .clip(MaterialTheme.shapes.extraSmall),
        )
        Text(
            text = film.title,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.height(51.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 3
        )
    }
}

/*
@Preview
@Composable
fun PosterItemPreview() {
    val film = LiteFilm(0, "Test Film", "", "", "", "", 0.0f, 0, false)
    PosterItem(film = film, onClick = { })
}*/

@Preview(name = "Light mode", showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark mode", showBackground = true)
@Composable
fun SurfacePreview() {
    Surface(
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        modifier = Modifier.padding(horizontal = 4.dp),
        shape = MaterialTheme.shapes.medium,
        // shadowElevation = 1.dp,
    ) {
        Text(text = "Surface colour test", modifier = Modifier.padding(12.dp))
    }
}