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


class CardsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                CardsMenu()
            }
        }
    }
}

@Composable
fun CardsMenu() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🃏 Cartas", style = MaterialTheme.typography.headlineMedium)

            Button(onClick = { /* TODO: Abrir Blackjack/21 */ }, modifier = Modifier.fillMaxWidth()) {
                Text("21")
            }

            Button(onClick = { /* TODO: Abrir Solitario */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Solitario")
            }
        }
    }
}
