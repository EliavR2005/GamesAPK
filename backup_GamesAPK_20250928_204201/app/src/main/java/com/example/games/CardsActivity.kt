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
    val context = LocalContext.current // ✅ Obtener el contexto aquí

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
            CardGameButton(
                title = "🎴 MEMORAMA",
                subtitle = "Juego de memoria con cartas",
                neonColor = Color(0xFFFF00FF),
                onClick = {
                    // ✅ Usar el contexto ya obtenido
                    context.startActivity(Intent(context, MemoramaActivity::class.java))
                }
            )

            CardGameButton(
                title = "🃏 PRÓXIMAMENTE",
                subtitle = "Más juegos de cartas en desarrollo",
                neonColor = Color(0xFF888888),
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
        // ✅ Agregar borde neón como en los otros botones
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

            // ✅ Cambiar a texto para evitar problemas de imports
            Text(
                text = "➔",
                color = neonColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}