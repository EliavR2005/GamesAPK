package com.example.games.trivia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games.ui.theme.GamesTheme
import kotlin.random.Random
import androidx.compose.foundation.BorderStroke


class TriviaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF000000) // Fondo negro neón
                ) {
                    TriviaScreen()
                }
            }
        }
    }
}

data class Question(
    val question: String,
    val options: List<String>,
    val answerIndex: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaScreen() {
    val questions = remember { loadQuestions() }
    var currentIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var answered by remember { mutableStateOf(false) }

    if (currentIndex >= questions.size) {
        TriviaResult(score, questions.size)
        return
    }

    val currentQuestion = questions[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Pregunta ${currentIndex + 1} / ${questions.size}",
            color = Color(0xFF00FFFF),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = currentQuestion.question,
            color = Color(0xFFFF00FF),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            currentQuestion.options.forEachIndexed { index, option ->
                Button(
                    onClick = {
                        if (!answered) {
                            answered = true
                            if (index == currentQuestion.answerIndex) score++
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (answered && index == currentQuestion.answerIndex) Color(0xFF00FF00) else Color(0xFF1A1A1A)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, Color(0xFFFF00FF))
                ) {
                    Text(option, color = Color.White, fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                currentIndex++
                answered = false
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FFFF))
        ) {
            Text("Siguiente", color = Color.Black, fontSize = 16.sp)
        }
    }
}

@Composable
fun TriviaResult(score: Int, total: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Trivia terminada!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF00FF)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tu puntaje: $score / $total",
            fontSize = 20.sp,
            color = Color(0xFF00FFFF),
            fontWeight = FontWeight.Bold
        )
    }
}

fun loadQuestions(): List<Question> {
    return listOf(
        Question("¿Qué lenguaje usa Android Compose?", listOf("Java", "Kotlin", "C++", "Python"), 1),
        Question("¿Qué es Jetpack Compose?", listOf("Un IDE", "Una librería UI", "Un lenguaje", "Una base de datos"), 1),
        Question("¿Qué comando crea un repositorio Git?", listOf("git init", "git start", "git create", "git build"), 0),
        Question("¿Qué hace 'git commit'?", listOf("Crea repositorio", "Guarda cambios", "Borra archivos", "Sube código"), 1),
        Question("¿Qué es un Activity?", listOf("Un archivo", "Una pantalla", "Una base de datos", "Un botón"), 1),
        Question("¿Qué hace 'git push'?", listOf("Sube código", "Borra repo", "Descarga código", "Inicializa Git"), 0),
        Question("¿Qué es un Composable?", listOf("Un tipo de Activity", "Una función UI", "Un archivo XML", "Una librería"), 1),
        Question("¿Qué archivo controla dependencias en Android?", listOf("manifest", "gradle", "build", "settings"), 1),
        Question("¿Qué es minSdkVersion?", listOf("Versión mínima Android", "Versión de Java", "Versión Gradle", "Versión Compose"), 0),
        Question("¿Qué hace 'git clone'?", listOf("Crea repo", "Copia repo", "Borra repo", "Compila repo"), 1)
    ).shuffled(Random(System.currentTimeMillis()))
}
