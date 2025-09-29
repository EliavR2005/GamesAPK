package com.example.games

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games.ui.theme.GamesTheme
import com.example.games.memorama.MemoramaActivity

class CardsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF000000)
                ) {
                    CardsMenu()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsMenu() {
    val context = LocalContext.current

    // Estado para animar el borde de Trivia
    var triviaSelected by remember { mutableStateOf(false) }
    val triviaColor by animateColorAsState(
        targetValue = if (triviaSelected) Color(0xFF00FFFF) else Color(0xFF00CCCC),
        animationSpec = tween(durationMillis = 800)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🎴 JUEGOS DE CARTAS",
                        color = Color(0xFFFF00FF),
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

            // Memorama
            CardGameButton(
                title = "🎴 MEMORAMA",
                subtitle = "Juego de memoria con cartas",
                neonColor = Color(0xFFFF00FF),
                height = 100.dp,
                borderWidth = 2.dp,
                onClick = {
                    context.startActivity(Intent(context, MemoramaActivity::class.java))
                }
            )

            // Trivia
            CardGameButton(
                title = "🧠 TRIVIA",
                subtitle = "10 preguntas de programación",
                neonColor = triviaColor,
                height = 120.dp,
                borderWidth = 3.dp,
                onClick = {
                    triviaSelected = !triviaSelected
                    context.startActivity(Intent(context, com.example.games.trivia.TriviaActivity::class.java))
                }
            )

            // Próximamente
            CardGameButton(
                title = "🃏 PRÓXIMAMENTE",
                subtitle = "Más juegos de cartas en desarrollo",
                neonColor = Color(0xFF888888),
                height = 100.dp,
                borderWidth = 2.dp,
                onClick = { }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardGameButton(
    title: String,
    subtitle: String,
    neonColor: Color,
    height: Dp,
    borderWidth: Dp,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(if (title.contains("TRIVIA")) 20.dp else 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        ),
        border = BorderStroke(borderWidth, neonColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (title.contains("TRIVIA")) 12.dp else 8.dp),
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
                    fontSize = if (title.contains("TRIVIA")) 20.sp else 18.sp,
                    fontWeight = FontWeight.ExtraBold
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
