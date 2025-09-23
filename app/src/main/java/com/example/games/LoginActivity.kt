package com.example.games

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DarkNeonTheme {
                LoginScreen(
                    onLoginSuccess = {
                        // acción dentro del composable, se ejecuta usando LocalContext
                    },
                    onRegisterClick = {
                        // acción dentro del composable, se ejecuta usando LocalContext
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current // ✅ aquí sí se puede usar
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.Black, Color(0xFF001F1F)))),
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
                "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF00FFFF)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                colors = neonTextFieldColors()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = neonTextFieldColors()
            )

            Button(
                onClick = {
                    if (username == "EliabR2005" && password == "panchopantera") {
                        // abrimos MainActivity desde aquí
                        context.startActivity(Intent(context, MainActivity::class.java))
                        (context as? ComponentActivity)?.finish()
                    } else {
                        Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00FFFF),
                    contentColor = Color.Black
                )
            ) {
                Text("Ingresar")
            }

            TextButton(
                onClick = {
                    context.startActivity(Intent(context, RegisterActivity::class.java))
                }
            ) {
                Text("¿No tienes cuenta? Regístrate", color = Color(0xFF00CED1))
            }
        }
    }
}
