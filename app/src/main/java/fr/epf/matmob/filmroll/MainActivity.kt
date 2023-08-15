package fr.epf.matmob.filmroll

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.epf.matmob.filmroll.state.FilmApplication
import fr.epf.matmob.filmroll.state.FilmViewModel
import fr.epf.matmob.filmroll.state.FilmViewModelFactory
import fr.epf.matmob.filmroll.state.RequestState
import fr.epf.matmob.filmroll.ui.components.FilmCarousel
import fr.epf.matmob.filmroll.ui.theme.FilmrollTheme

private const val TAG = "HomeActivity"

class MainActivity : ComponentActivity() {
    private val viewModel: FilmViewModel by viewModels {
        FilmViewModelFactory((application as FilmApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPopularFilms()
        setContent {
            FilmrollTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    HomeStructure(viewModel = viewModel, context = this)
                }
            }
        }
    }
}

@Composable
fun HomeStructure(viewModel: FilmViewModel, context: Context) {
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    Scaffold(bottomBar = { AppNavigationBar(selectedItem) { selectedItem = it } },
        topBar = { HomeTopBar() }) { padValues ->
        when (selectedItem) {
            0 -> DisplayHomeScreen(
                viewModel = viewModel, context = context, modifier = Modifier.padding(padValues)
            )

            1 -> FavouritesList(viewModel = viewModel, Modifier.padding(padValues))

            /* Le scan de QR code reste une activité car la vie de la vue est liée à l'activité,
            pas au composable. Si seul le composable est utilisé, la caméra ne se ferme pas lors
            du changement d'affichage et ne se ré-affiche pas lors du retour au scan. */
            2 -> context.startActivity(Intent(context, QRScanActivity::class.java))
        }
    }
}

@Composable
fun DisplayHomeScreen(viewModel: FilmViewModel, context: Context, modifier: Modifier = Modifier) {
    val popularFilms by viewModel.popularFilms.observeAsState(initial = emptyList())
    val popularFilmsStatus by viewModel.popularFilmsStatus.observeAsState(initial = RequestState.LOADING)
    LazyColumn(modifier = modifier) {
        item {
            SearchBar(context)
            FilmCarousel(
                films = popularFilms,
                title = stringResource(R.string.popular_films_title),
                modifier = Modifier.padding(bottom = 2.dp),
                requestState = popularFilmsStatus,
            )
        }
    }
}

@Composable
fun SearchBar(context: Context) {
    val (value, onValueChange) = remember { mutableStateOf("") }
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        shape = MaterialTheme.shapes.extraLarge,
        placeholder = { Text(text = stringResource(R.string.searchbar_placeholder)) },
        trailingIcon = {
            Icon(imageVector = Icons.Filled.Clear,
                contentDescription = null,
                modifier = Modifier.clickable { onValueChange("") })
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search, contentDescription = null
            )
        },
        keyboardActions = KeyboardActions(onSearch = {
            if (value.isNotBlank()) {
                val intent = Intent(context, FilmResultsList::class.java)
                intent.putExtra("searchQuery", value.trim())
                context.startActivity(intent)
            }
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 64.dp, bottom = 80.dp)
            .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.extraLarge),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun AppNavigationBar(selectedIndex: Int, onSelectItem: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(icon = {
            Icon(
                Icons.Default.Home,
                contentDescription = stringResource(R.string.home_icon_description)
            )
        }, label = {
            Text(
                text = stringResource(R.string.home_icon_label),
                style = MaterialTheme.typography.labelSmall
            )
        }, selected = selectedIndex == 0, onClick = { onSelectItem(0) })
        NavigationBarItem(icon = {
            Icon(
                Icons.Default.Favorite,
                contentDescription = stringResource(R.string.favourite_icon_description)
            )
        }, label = {
            Text(
                text = stringResource(R.string.favourites_icon_label),
                style = MaterialTheme.typography.labelSmall
            )
        }, selected = selectedIndex == 1, onClick = { onSelectItem(1) })
        NavigationBarItem(icon = {
            Icon(
                Icons.Default.QrCodeScanner,
                contentDescription = stringResource(R.string.qr_code_icon_description)
            )
        }, label = {
            Text(
                text = stringResource(R.string.qr_scan_icon_label),
                style = MaterialTheme.typography.labelSmall
            )
        }, selected = selectedIndex == 2, onClick = { onSelectItem(2) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    CenterAlignedTopAppBar(title = {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleMedium
        )
    })
}

@Preview
@Composable
fun NavigationBarPreview() {
    FilmrollTheme {
        AppNavigationBar(0) {}
    }
}