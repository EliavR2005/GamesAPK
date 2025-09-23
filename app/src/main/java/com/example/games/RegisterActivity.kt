package com.example.games

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DarkNeonTheme {
                RegisterScreen()
            }
        }
    }
}

@Composable
fun RegisterScreen() {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color.Black, Color(0xFF001F1F))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Registro",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF00FFFF)
            )

            // Campo usuario
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                colors = neonTextFieldColors()
            )

            // Campo email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                colors = neonTextFieldColors()
            )

            // Campo contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = neonTextFieldColors()
            )

            // Botón de registro
            Button(
                onClick = { /* lógica de registro */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00FFFF),
                    contentColor = Color.Black
                )
            ) {
                Text("Registrarse")
            }

            TextButton(onClick = { /* volver a login */ }) {
                Text("¿Ya tienes cuenta? Inicia sesión", color = Color(0xFF00CED1))
            }
        }
    }
}

@Composable
fun DarkNeonTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF00FFFF), // aqua neon
            background = Color.Black,
            surface = Color(0xFF001F1F),
            onPrimary = Color.Black,
            onBackground = Color(0xFF00FFFF),
            onSurface = Color(0xFF00FFFF)
        ),
        content = content
    )
}

@Composable
fun neonTextFieldColors() = TextFieldDefaults.colors(
    focusedIndicatorColor = Color(0xFF00FFFF),
    unfocusedIndicatorColor = Color(0xFF00CED1),
    focusedLabelColor = Color(0xFF00FFFF),
    unfocusedLabelColor = Color(0xFF00CED1),
    cursorColor = Color(0xFF00FFFF)
)
