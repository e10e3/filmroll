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
import androidx.compose.material3.Button
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.epf.matmob.filmroll.state.FilmApplication
import fr.epf.matmob.filmroll.state.FilmViewModel
import fr.epf.matmob.filmroll.state.FilmViewModelFactory
import fr.epf.matmob.filmroll.ui.components.FilmCarousel
import fr.epf.matmob.filmroll.ui.theme.FilmrollTheme

private const val TAG = "HomeActivity"

class MainActivity : ComponentActivity() {
    private val viewModel: FilmViewModel by viewModels {
        FilmViewModelFactory((application as FilmApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    Scaffold(bottomBar = { AppNavigationBar() }, topBar = { HomeTopBar() }) { padValues ->
        DisplayHomeScreen(
            viewModel = viewModel, context = context, modifier = Modifier.padding(padValues)
        )
    }
}

@Composable
fun DisplayHomeScreen(viewModel: FilmViewModel, context: Context, modifier: Modifier = Modifier) {
    viewModel.getPopularFilms()
    LazyColumn(modifier = modifier) {
        item {
            val (value, onValueChange) = remember { mutableStateOf("") }
            TextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                shape = MaterialTheme.shapes.extraLarge,
                placeholder = { Text(text = "Search a film") },
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
                    val intent = Intent(context, FilmResultsList::class.java)
                    intent.putExtra("searchQuery", value)
                    context.startActivity(intent)
                }),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 64.dp, bottom = 96.dp),
                // .shadow(elevation = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            val popularFilms by viewModel.popularFilms.observeAsState()
            popularFilms?.let {
                FilmCarousel(films = it, title = "Currently popular films")
            }

            Button(onClick = {
                context.startActivity(Intent(context, QRScanActivity::class.java))
            }) {
                Text(text = "Scan a QR code")
            }
        }
    }
}

data class NavItem(val label: String, val icon: ImageVector)

@Composable
fun AppNavigationBar() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Favourites", Icons.Default.Favorite),
        NavItem("QR Scan", Icons.Default.QrCodeScanner)
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                selected = selectedItem == index,
                onClick = { selectedItem = index })
        }
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
        AppNavigationBar()
    }
}