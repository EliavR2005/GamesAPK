package com.example.games

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games.tictactoe.TicTacToeActivity
import com.example.games.ui.theme.GamesTheme
import com.example.games.sudoku.SudokuActivity

class PuzzleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF000000)
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
    val context = LocalContext.current // ✅ Contexto obtenido correctamente aquí

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🧩 JUEGOS PUZZLE",
                        color = Color(0xFF00FF00),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            PuzzleGameButton(
                title = "❌⭕ TIC TAC TOE",
                subtitle = "Juego de 3 en raya",
                neonColor = Color(0xFF00FF00),
                onClick = {
                    context.startActivity(Intent(context, TicTacToeActivity::class.java))
                }
            )

            PuzzleGameButton(
                title = "🔢 SUDOKU",
                subtitle = "Rompecabezas numérico",
                neonColor = Color(0xFF0088FF),
                onClick = {
                    context.startActivity(Intent(context, SudokuActivity::class.java))
                }
            )

            PuzzleGameButton(
                title = "🔄 PRÓXIMAMENTE",
                subtitle = "Más juegos en desarrollo",
                neonColor = Color(0xFF888888),
                onClick = { }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleGameButton(
    title: String,
    subtitle: String,
    neonColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        ),
        border = BorderStroke(2.dp, neonColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = neonColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = subtitle,
                    color = Color(0xFFCCCCCC),
                    fontSize = 14.sp
                )
            }

            Text(
                text = "➔",
                color = neonColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}