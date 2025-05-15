package org.example.pokedexmultiplataforma

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.pokedexmultiplataforma.compose.AppContent

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Pokédex Desktop") {
        AppContent()
    }
}