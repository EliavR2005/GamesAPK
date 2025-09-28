package com.example.games

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games.ui.theme.GamesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF000000)
                ) {
                    CategoryMenu()
                }
            }
        }
    }
}

@Composable
fun CategoryMenu() {
    val context = LocalContext.current

    Scaffold(
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎮",
                fontSize = 64.sp,
                color = Color(0xFF00FFFF),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "SELECCIONA CATEGORÍA",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF00FFFF),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 50.dp)
            )

            NeonCategoryButton(
                text = "🧩 PUZZLE",
                neonColor = Color(0xFF00FF00),
                onClick = {
                    context.startActivity(Intent(context, PuzzleActivity::class.java))
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            NeonCategoryButton(
                text = "🎴 CARTAS",
                neonColor = Color(0xFFFF00FF),
                onClick = {
                    context.startActivity(Intent(context, CardsActivity::class.java))
                }
            )
        }
    }
}

@Composable
fun NeonCategoryButton(
    text: String,
    neonColor: Color,
    onClick: () -> Unit
) {
    // Usar OutlinedButton en lugar de Button para tener control sobre el borde
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .height(85.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFF1A1A1A),
            contentColor = neonColor
        ),
        border = BorderStroke(3.dp, neonColor)
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
    }
}
