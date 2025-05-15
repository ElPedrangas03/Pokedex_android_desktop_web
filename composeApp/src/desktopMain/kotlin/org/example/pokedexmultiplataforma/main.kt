package org.example.pokedexmultiplataforma

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.pokedexmultiplataforma.compose.AppContent

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Pokédex Desktop",
        state = rememberWindowState(
            size = DpSize(width = 600.dp, height = 900.dp)
        )
    ) {
        AppContent()
    }
}