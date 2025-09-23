package com.example.games

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.games.ui.theme.GamesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamesTheme {
                CategoryMenu()
            }
        }
    }
}


@Composable
fun CategoryMenu() {
    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🎮 Categorías", style = MaterialTheme.typography.headlineMedium)

            Button(
                onClick = { context.startActivity(Intent(context, ArcadeActivity::class.java)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Arcade")
            }

            Button(
                onClick = { context.startActivity(Intent(context, PuzzleActivity::class.java)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                 Text("Puzzle")
            }

            Button(
                onClick = { context.startActivity(Intent(context, CardsActivity::class.java)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cartas")
            }
        }
    }
}
