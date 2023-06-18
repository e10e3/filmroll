package fr.epf.matmob.filmroll

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import fr.epf.matmob.filmroll.state.FilmViewModel
import fr.epf.matmob.filmroll.ui.components.VerticalFilmList

@Composable
fun FavouritesList(viewModel: FilmViewModel, modifier: Modifier = Modifier) {
    val favouriteFilms by viewModel.favouriteLiteFilms.observeAsState(initial = emptyList())
     VerticalFilmList(filmList = favouriteFilms, modifier = modifier)
}