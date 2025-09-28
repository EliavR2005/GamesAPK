package com.example.games

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.games.DarkNeonTheme
import com.example.games.neonTextFieldColors


class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userManager = UserManager(this)

        setContent {
            DarkNeonTheme {
                RegisterScreen(userManager = userManager) { finish() }
            }
        }
    }
}

@Composable
fun RegisterScreen(
    userManager: UserManager,
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registro de usuario", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        OutlinedTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
        }

        Button(
            onClick = {
                when {
                    password != confirm -> error = "Las contraseñas no coinciden"
                    email.isEmpty() || password.isEmpty() -> error = "Llena todos los campos"
                    userManager.register(email, password) -> {
                        Toast.makeText(context, "Usuario registrado ✅", Toast.LENGTH_SHORT).show()
                        onRegisterSuccess()
                    }
                    else -> error = "El usuario ya existe"
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Registrar")
        }
    }
}
