package fr.epf.matmob.filmroll

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.epf.matmob.filmroll.state.FilmViewModel
import fr.epf.matmob.filmroll.ui.components.StatusScreen
import fr.epf.matmob.filmroll.ui.components.VerticalFilmList

@Composable
fun FavouritesList(viewModel: FilmViewModel, modifier: Modifier = Modifier) {
    val favouriteFilms by viewModel.favouriteLiteFilms.observeAsState(initial = emptyList())
    if (favouriteFilms.isEmpty()) {
        NoFavourites(modifier = modifier)
    } else {
        VerticalFilmList(filmList = favouriteFilms, modifier = modifier)
    }
}

@Composable
fun NoFavourites(modifier: Modifier = Modifier) {
    StatusScreen(
        text = "No favourites yet!", icon = {
            Icon(
                Icons.Default.HeartBroken,
                contentDescription = "A heart broken in halves",
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .size(48.dp)
            )
        }, modifier = modifier
    )
}