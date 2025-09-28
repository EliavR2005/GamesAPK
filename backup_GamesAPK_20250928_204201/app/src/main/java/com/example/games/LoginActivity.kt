package com.example.games

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
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
import androidx.core.content.ContextCompat

class LoginActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userManager = UserManager(this)

        setContent {
            DarkNeonTheme {
                LoginScreen(
                    userManager = userManager,
                    onLoginSuccess = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    },
                    onRegisterClick = {
                        startActivity(Intent(this, RegisterActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    userManager: UserManager,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    // Configuración biométrica
    val biometricManager = BiometricManager.from(context)
    val canAuth = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)

    val executor = ContextCompat.getMainExecutor(context)
    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(context, "Acceso con huella exitoso", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(context, "Error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context, "Huella no reconocida", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val promptInfo = PromptInfo.Builder()
        .setTitle("Acceso con huella")
        .setSubtitle("Usa tu huella para iniciar sesión")
        .setNegativeButtonText("Cancelar")
        .build()

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
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
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

            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }

            // Botón login normal
            Button(
                onClick = {
                    if (userManager.login(email, password)) {
                        onLoginSuccess()
                    } else {
                        error = "Usuario o contraseña incorrectos"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar")
            }

            // Botón login con huella
            if (canAuth == BiometricManager.BIOMETRIC_SUCCESS) {
                Button(
                    onClick = { biometricPrompt.authenticate(promptInfo) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00CED1))
                ) {
                    Text("Ingresar con huella", color = Color.Black)
                }
            }

            TextButton(onClick = onRegisterClick) {
                Text("¿No tienes cuenta? Regístrate", color = Color(0xFF00CED1))
            }
        }
    }
}
