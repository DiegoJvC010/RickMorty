package org.example.kotlinmulti

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import org.example.kotlinmulti.ui.CharacterListScreen

@Composable
fun App() {
    // Esquema de colores personalizado para Rick and Morty
    val colorScheme = lightColorScheme(
        primary = Color(0xFF97CE4C),         // Verde Rick and Morty
        onPrimary = Color.White,
        primaryContainer = Color(0xFF3C3E44), // Gris oscuro
        onPrimaryContainer = Color.White,
        secondary = Color(0xFF00B5CC),        // Azul portal
        onSecondary = Color.White,
        tertiary = Color(0xFFFF9800),         // Naranja
        background = Color(0xFFF5F5F5),
        surface = Color.White
    )

    MaterialTheme(colorScheme = colorScheme) {
        Surface {
            CharacterListScreen()
        }
    }
}