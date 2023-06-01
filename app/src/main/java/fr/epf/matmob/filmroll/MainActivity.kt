package fr.epf.matmob.filmroll

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import fr.epf.matmob.filmroll.ui.theme.FilmrollTheme

private const val TAG = "HomeActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilmrollTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Button(onClick = {
                            val intent = Intent(this@MainActivity, FilmResultsList::class.java)
                            intent.putExtra("searchQuery", "monty python")
                            startActivity(intent)
                        }) {
                            Text(text = "Search for films")
                        }
                    }
                }
            }
        }
    }
}