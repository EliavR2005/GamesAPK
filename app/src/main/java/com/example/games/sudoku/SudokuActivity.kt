package com.example.games.sudoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.games.ui.theme.GamesTheme
import com.example.games.sudoku.common.CountUpTimer
import com.example.games.sudoku.common.GameEngine
import com.example.games.sudoku.common.Difficulty
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class SudokuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                SudokuScreen()
            }
        }
    }
}

@Composable
fun SudokuScreen() {
    val scope = rememberCoroutineScope()
    val engine = remember { GameEngine(CountUpTimer()) }
    var loaded by remember { mutableStateOf(false) }
    var boardSummary by remember { mutableStateOf("No iniciado") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("🧩 Sudoku", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            scope.launch {
                engine.init(Difficulty.Easy) // usa la extensión missingDigits
                val current = engine.currentBoard.first()
                boardSummary = "Tablero cargado: ${current.size} parent cells (cada parent tiene ${current.firstOrNull()?.subCells?.size ?: 0} subcells)"
                loaded = true
            }
        }) {
            Text("Iniciar Sudoku (Easy)")
        }

        Spacer(Modifier.height(12.dp))

        Text(boardSummary)
    }
}
