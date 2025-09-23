package com.example.games.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games.ui.theme.GamesTheme

class TicTacToeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeScreen()
                }
            }
        }
    }
}

@Composable
fun TicTacToeScreen() {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var activePlayer by remember { mutableStateOf("X") }
    var player1Wins by remember { mutableStateOf(0) }
    var player2Wins by remember { mutableStateOf(0) }
    var draws by remember { mutableStateOf(0) }
    var gameStatus by remember { mutableStateOf("Turno del jugador X") }

    fun checkWinner(): String? {
        val combos = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // filas
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // columnas
            listOf(0, 4, 8), listOf(2, 4, 6) // diagonales
        )

        for (combo in combos) {
            val (a, b, c) = combo
            if (board[a].isNotEmpty() && board[a] == board[b] && board[a] == board[c]) {
                return board[a]
            }
        }

        // Verificar empate
        if (board.all { it.isNotEmpty() }) {
            return "Draw"
        }

        return null
    }

    fun makeMove(index: Int) {
        if (board[index].isEmpty() && checkWinner() == null) {
            board = board.toMutableList().also { it[index] = activePlayer }

            val winner = checkWinner()
            when {
                winner == "X" -> {
                    player1Wins++
                    gameStatus = "¡Jugador X gana!"
                }
                winner == "O" -> {
                    player2Wins++
                    gameStatus = "¡Jugador O gana!"
                }
                winner == "Draw" -> {
                    draws++
                    gameStatus = "¡Empate!"
                }
                else -> {
                    activePlayer = if (activePlayer == "X") "O" else "X"
                    gameStatus = "Turno del jugador $activePlayer"
                }
            }
        }
    }

    fun restartGame() {
        board = List(9) { "" }
        activePlayer = "X"
        gameStatus = "Turno del jugador X"
    }

    fun resetAll() {
        restartGame()
        player1Wins = 0
        player2Wins = 0
        draws = 0
    }

    val winner = checkWinner()
    val isGameOver = winner != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Tic Tac Toe",
            fontSize = 32.sp,
            color = Color.Cyan,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        // Estado del juego
        Text(
            text = gameStatus,
            fontSize = 18.sp,
            color = when {
                winner == "X" -> Color.Green
                winner == "O" -> Color.Yellow
                winner == "Draw" -> Color.LightGray
                else -> Color.White
            },
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(24.dp))

        // Tablero 3x3
        Column(
            modifier = Modifier.background(Color.DarkGray, shape = MaterialTheme.shapes.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0 until 3) {
                Row {
                    for (col in 0 until 3) {
                        val index = row * 3 + col
                        val cellColor = when (board[index]) {
                            "X" -> Color.Blue.copy(alpha = 0.3f)
                            "O" -> Color.Red.copy(alpha = 0.3f)
                            else -> Color.DarkGray
                        }

                        Button(
                            onClick = { makeMove(index) },
                            modifier = Modifier
                                .size(80.dp)
                                .padding(2.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = cellColor,
                                disabledContainerColor = cellColor
                            ),
                            enabled = !isGameOver && board[index].isEmpty()
                        ) {
                            Text(
                                text = board[index],
                                fontSize = 28.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Controles del juego
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { restartGame() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Nuevo Juego")
            }

            Button(
                onClick = { resetAll() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Reiniciar Todo")
            }
        }

        Spacer(Modifier.height(24.dp))

        // Estadísticas
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Estadísticas", color = Color.Cyan, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                Text("Jugador X: $player1Wins", color = Color.Blue)
                Text("Jugador O: $player2Wins", color = Color.Red)
                Text("Empates: $draws", color = Color.LightGray)
            }
        }
    }
}