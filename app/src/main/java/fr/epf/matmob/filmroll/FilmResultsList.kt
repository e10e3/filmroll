package fr.epf.matmob.filmroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import fr.epf.matmob.filmroll.state.FilmApplication
import fr.epf.matmob.filmroll.state.FilmViewModel
import fr.epf.matmob.filmroll.state.FilmViewModelFactory
import fr.epf.matmob.filmroll.ui.components.VerticalFilmList
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
                    SearchScreenStructure(viewModel = viewModel, query = query!!)
                }
            }
        }
    }
}

@Composable
fun SearchScreenStructure(viewModel: FilmViewModel, query: String) {
    val foundFilms by viewModel.foundFilms.observeAsState()
    Scaffold(topBar = { ResultsTopBar(query = query) }) { padVal ->
        foundFilms?.let { VerticalFilmList(filmList = it, modifier = Modifier.padding(padVal)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsTopBar(query: String) {
    TopAppBar(title = { Text(text = "Query: $query", style = MaterialTheme.typography.headlineSmall) })
}