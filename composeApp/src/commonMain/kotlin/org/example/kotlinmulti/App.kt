package org.example.kotlinmulti

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import org.example.kotlinmulti.ui.CharacterListScreen

@Composable
fun App() {
    //define un esquema de colores claro personalizado
    val colorScheme = lightColorScheme(
        primary = Color(0xFF97CE4C),
        onPrimary = Color.White,
        primaryContainer = Color(0xFF3C3E44),
        onPrimaryContainer = Color.White,
        secondary = Color(0xFF00B5CC),
        onSecondary = Color.White,
        tertiary = Color(0xFFFF9800),
        background = Color(0xFFF5F5F5),
        surface = Color.White
    )

    //aplica el esquema de colores a toda la app
    MaterialTheme(colorScheme = colorScheme) {
        //surface actua como contenedor principal respetando el tema
        Surface {
            //invoca la pantalla que muestra la lista de personajes
            CharacterListScreen()
        }
    }
}