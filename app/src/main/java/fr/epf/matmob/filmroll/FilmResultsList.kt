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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import fr.epf.matmob.filmroll.state.FilmApplication
import fr.epf.matmob.filmroll.state.FilmViewModel
import fr.epf.matmob.filmroll.state.FilmViewModelFactory
import fr.epf.matmob.filmroll.state.RequestState
import fr.epf.matmob.filmroll.ui.components.ErrorScreen
import fr.epf.matmob.filmroll.ui.components.LoadingScreen
import fr.epf.matmob.filmroll.ui.components.NotFoundScreen
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
    val foundFilms by viewModel.foundFilms.observeAsState(initial = emptyList())
    val foundFilmsStatus by viewModel.foundFilmsStatus.observeAsState(initial = RequestState.LOADING)
    Scaffold(topBar = { ResultsTopBar(query = query) }) { padVal ->
        when (foundFilmsStatus) {
            RequestState.LOADING -> LoadingScreen(modifier = Modifier.padding(paddingValues = padVal))
            RequestState.SUCCESS -> VerticalFilmList(
                filmList = foundFilms, modifier = Modifier.padding(
                    paddingValues = padVal
                )
            )

            RequestState.NOT_FOUND -> NotFoundScreen(modifier = Modifier.padding(paddingValues = padVal))
            RequestState.ERROR -> ErrorScreen(modifier = Modifier.padding(paddingValues = padVal))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsTopBar(query: String) {
    TopAppBar(title = {
        Text(
            text = stringResource(id = R.string.search_query_label, query),
            style = MaterialTheme.typography.headlineSmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    })
}