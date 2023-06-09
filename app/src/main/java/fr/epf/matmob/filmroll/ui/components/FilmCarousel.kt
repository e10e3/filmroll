package fr.epf.matmob.filmroll.ui.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
fun FilmCarousel(films: List<LiteFilm>, title: String) {
    val activity = LocalContext.current as Activity
    val columnWidth = 111.dp
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(films) {
                Column(modifier = Modifier
                    .clickable {
                        activity.startActivity(
                            Intent(
                                activity, FilmCardActivity::class.java
                            ).putExtra("TMDBFilmId", it.tmdbId)
                        )
                    }
                    .width(columnWidth)) {
                    GlideImage(
                        model = if (it.posterPath.isEmpty()) {
                            "https://placehold.co/342x513.png"
                        } else {
                            "https://image.tmdb.org/t/p/w342${it.posterPath}"
                        },
                        contentDescription = "${it.title}'s poster image",
                        modifier = Modifier.fillMaxWidth().height(175.dp)
                    )
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.height(50.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3
                    )
                }
            }
        }
    }
}