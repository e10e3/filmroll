package fr.epf.matmob.filmroll.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun StatusScreen(text: String, icon: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, textAlign = TextAlign.Center)
        icon()
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    StatusScreen(
        text = "Loading…", icon = {
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }, modifier = modifier
    )
}

@Composable
fun NotFoundScreen(modifier: Modifier = Modifier) {
    StatusScreen(
        text = "No film found", icon = {
            Icon(
                Icons.Default.TravelExplore,
                contentDescription = "Globe icon with a looking glass.",
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .size(48.dp)
            )
        }, modifier = modifier
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    StatusScreen(
        text = "Error!", icon = {
            Icon(
                imageVector = Icons.Default.SentimentVeryDissatisfied,
                contentDescription = "A frowning face",
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .size(48.dp)
            )
        }, modifier = modifier
    )
}