package com.example.games

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.games.tictactoe.TicTacToeActivity
import com.example.games.ui.theme.GamesTheme

class PuzzleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PuzzleMenu()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleMenu() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar( // Usar CenterAlignedTopAppBar en lugar de TopAppBar
                title = { Text("🧩 Puzzle Games") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TicTacToeButton()

            SudokuButton()

            // Puedes agregar más juegos aquí
            Button(onClick = { /* TODO: Otro juego */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Próximamente...")
            }
        }
    }
}

@Composable
fun TicTacToeButton() {
    val context = LocalContext.current

    Button(
        onClick = {
            try {
                val intent = Intent(context, TicTacToeActivity::class.java)
                context.startActivity(intent)
            } catch (e: Exception) {
                // Manejar error (puedes mostrar un Toast)
                e.printStackTrace()
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Tic Tac Toe")
    }
}

@Composable
fun SudokuButton() {
    val context = LocalContext.current

    Button(
        onClick = {
            // TODO: Implementar SudokuActivity
            // context.startActivity(Intent(context, SudokuActivity::class.java))
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Sudoku")
    }
}