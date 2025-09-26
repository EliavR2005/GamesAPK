package com.example.games

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
