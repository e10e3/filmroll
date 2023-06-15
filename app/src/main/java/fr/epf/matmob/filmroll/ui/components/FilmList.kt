package fr.epf.matmob.filmroll.ui.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import fr.epf.matmob.filmroll.FilmCardActivity
import fr.epf.matmob.filmroll.R
import fr.epf.matmob.filmroll.model.LiteFilm

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilmListItem(film: LiteFilm) {
    val activity = LocalContext.current as Activity
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp), onClick = {
        activity.startActivity(
            Intent(
                activity, FilmCardActivity::class.java
            ).putExtra("TMDBFilmId", film.tmdbId)
        )
    }) {
        Row(modifier = Modifier.padding(12.dp)) {
            GlideImage(
                model = if (film.posterPath.isEmpty()) {
                    "https://placehold.co/342x513.png"
                } else {
                    "https://image.tmdb.org/t/p/w342${film.posterPath}"
                }, contentDescription = "${film.title}'s poster image",
                // modifier = Modifier
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .width(85.dp)
            )
            Column(
                modifier = Modifier.padding(
                    start = 8.dp
                )
            ) {
                Text(
                    text = film.title,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    lineHeight = 25.sp
                )
                Text(
                    text = if (film.releaseDate.isNotBlank()) film.releaseDate.substring(
                        0, 4
                    ) else stringResource(R.string.unknown_date_replacement),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = film.synopsis,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 4,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun VerticalFilmList(filmList: List<LiteFilm>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(filmList) {
            FilmListItem(film = it)
        }
    }
}