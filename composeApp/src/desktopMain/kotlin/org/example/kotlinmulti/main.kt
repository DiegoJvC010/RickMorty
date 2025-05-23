package org.example.kotlinmulti

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.kotlinmulti.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "RickMorty",
    ) {
        App()
    }
}