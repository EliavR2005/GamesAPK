package com.example.games

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.games.ui.theme.GamesTheme
import androidx.compose.runtime.Composable


class ArcadeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                ArcadeMenu()
            }
        }
    }
}

@Composable
fun ArcadeMenu() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("👾 Arcade", style = MaterialTheme.typography.headlineMedium)

            Button(onClick = { /* TODO: Abrir SnakeActivity */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Snake")
            }

            Button(onClick = { /* TODO: Abrir PacmanActivity */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Pacman")
            }

            Button(onClick = { /* TODO: Abrir BreakoutActivity */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Breakout")
            }
        }
    }
}
