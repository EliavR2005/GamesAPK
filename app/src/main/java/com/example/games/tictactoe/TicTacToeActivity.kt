package com.example.games.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.games.ui.theme.GamesTheme
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState


class TicTacToeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                PantallaTicTacToe()
            }
        }
    }
}

@Composable
fun PantallaTicTacToe() {
    var tablero by remember { mutableStateOf(List(9) { "" }) }
    var jugadorActivo by remember { mutableStateOf("X") }
    var victoriasJugador1 by remember { mutableStateOf(0) }
    var victoriasJugador2 by remember { mutableStateOf(0) }
    var empates by remember { mutableStateOf(0) }
    var estadoJuego by remember { mutableStateOf("Turno del jugador X") }
    var mostrarDialogo by remember { mutableStateOf(false) }

    fun verificarGanador(): String? {
        val combinaciones = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // filas
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // columnas
            listOf(0, 4, 8), listOf(2, 4, 6) // diagonales
        )

        for (combo in combinaciones) {
            val (a, b, c) = combo
            if (tablero[a].isNotEmpty() && tablero[a] == tablero[b] && tablero[a] == tablero[c]) {
                return tablero[a]
            }
        }

        // Verificar empate
        if (tablero.all { it.isNotEmpty() }) {
            return "Empate"
        }

        return null
    }

    fun realizarMovimiento(indice: Int) {
        if (tablero[indice].isEmpty() && verificarGanador() == null) {
            tablero = tablero.toMutableList().also { it[indice] = jugadorActivo }

            val ganador = verificarGanador()
            when {
                ganador == "X" -> {
                    victoriasJugador1++
                    estadoJuego = "¡Jugador X gana!"
                    mostrarDialogo = true
                }

                ganador == "O" -> {
                    victoriasJugador2++
                    estadoJuego = "¡Jugador O gana!"
                    mostrarDialogo = true
                }

                ganador == "Empate" -> {
                    empates++
                    estadoJuego = "¡Empate!"
                    mostrarDialogo = true
                }

                else -> {
                    jugadorActivo = if (jugadorActivo == "X") "O" else "X"
                    estadoJuego = "Turno del jugador $jugadorActivo"
                }
            }
        }
    }

    fun reiniciarJuego() {
        tablero = List(9) { "" }
        jugadorActivo = "X"
        estadoJuego = "Turno del jugador X"
        mostrarDialogo = false
    }

    fun reiniciarTodo() {
        reiniciarJuego()
        victoriasJugador1 = 0
        victoriasJugador2 = 0
        empates = 0
        mostrarDialogo = false
    }

    val ganador = verificarGanador()
    val juegoTerminado = ganador != null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título con estilo neon
            Text(
                text = "Tic Tac Toe",
                fontSize = 42.sp,
                color = Color.Cyan,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Estado del juego
            Text(
                text = estadoJuego,
                fontSize = 20.sp,
                color = when {
                    ganador == "X" -> Color(0xFF00FF88)
                    ganador == "O" -> Color(0xFFFF0088)
                    ganador == "Empate" -> Color(0xFFFFFF88)
                    else -> Color(0xFF88FFFF)
                },
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(32.dp))

            // Tablero 3x3 con bordes visibles
            Column(
                modifier = Modifier
                    .border(3.dp, Color.Cyan, MaterialTheme.shapes.medium)
                    .background(Color(0xFF1A1A1A))
            ) {
                for (fila in 0 until 3) {
                    Row {
                        for (columna in 0 until 3) {
                            val indice = fila * 3 + columna

                            val bordeDerecho = if (columna < 2) 1.5.dp else 0.dp
                            val bordeInferior = if (fila < 2) 1.5.dp else 0.dp

                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .border(bordeDerecho, Color.Cyan.copy(alpha = 0.6f))
                                    .border(bordeInferior, Color.Cyan.copy(alpha = 0.6f))
                                    .clickable(
                                        enabled = !juegoTerminado && tablero[indice].isEmpty()
                                    ) {
                                        realizarMovimiento(indice)
                                    }
                                    .background(
                                        when (tablero[indice]) {
                                            "X" -> Color(0x3300FF88)
                                            "O" -> Color(0x33FF0088)
                                            else -> Color.Transparent
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tablero[indice],
                                    fontSize = 48.sp,
                                    color = when (tablero[indice]) {
                                        "X" -> Color(0xFF00FF88)
                                        "O" -> Color(0xFFFF0088)
                                        else -> Color.Transparent
                                    },
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // Botones con íconos CORREGIDOS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Botón "Nuevo Juego" - Usar Icons.Filled.Refresh
                Button(
                    onClick = { reiniciarJuego() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FF88))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh, // ✅ Ícono correcto
                        contentDescription = "Nuevo juego",
                        tint = Color.Black
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Nuevo Juego", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                // Botón "Reiniciar Todo" - Usar Icons.Filled.RestartAlt
                Button(
                    onClick = { reiniciarTodo() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0088))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh, // ✅ Ícono correcto
                        contentDescription = "Reiniciar todo",
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Reiniciar Todo", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Estadísticas
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .border(2.dp, Color.Cyan, MaterialTheme.shapes.medium),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "ESTADÍSTICAS",
                        color = Color.Cyan,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Jugador X: $victoriasJugador1",
                        color = Color(0xFF00FF88),
                        fontSize = 16.sp
                    )
                    Text(
                        "Jugador O: $victoriasJugador2",
                        color = Color(0xFFFF0088),
                        fontSize = 16.sp
                    )
                    Text(
                        "Empates: $empates",
                        color = Color(0xFFFFFF88),
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Diálogo de fin de juego
        if (mostrarDialogo) {
            Dialog(
                onDismissRequest = { mostrarDialogo = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.95f) // Ocupa 95% del ancho de pantalla
                        .wrapContentHeight() // Se ajusta en altura
                        .border(3.dp, Color.Cyan, MaterialTheme.shapes.large),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()), // Permite scroll si la pantalla es pequeña
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "¡FIN DEL JUEGO!",
                            fontSize = 24.sp,
                            color = Color.Cyan,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = estadoJuego,
                            fontSize = 20.sp,
                            color = when (ganador) {
                                "X" -> Color(0xFF00FF88)
                                "O" -> Color(0xFFFF0088)
                                "Empate" -> Color(0xFFFFFF88)
                                else -> Color.White
                            },
                            fontWeight = FontWeight.Medium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                modifier = Modifier.weight(1f).padding(4.dp), // Se adapta
                                onClick = {
                                    reiniciarJuego()
                                    mostrarDialogo = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF00FF88
                                    )
                                )
                            ) {
                                Text(
                                    "Jugar Otra Vez",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                modifier = Modifier.weight(1f).padding(4.dp), // Se adapta
                                onClick = {
                                    reiniciarTodo()
                                    mostrarDialogo = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFFF0088
                                    )
                                )
                            ) {
                                Text(
                                    "Reiniciar Todo",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}