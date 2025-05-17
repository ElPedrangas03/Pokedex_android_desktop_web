package org.example.pokedexmultiplataforma.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
actual fun PokemonImage(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Pokemon Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
