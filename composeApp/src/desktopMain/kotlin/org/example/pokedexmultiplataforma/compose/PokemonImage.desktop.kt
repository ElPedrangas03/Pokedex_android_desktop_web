package org.example.pokedexmultiplataforma.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.loadImageBitmap
import java.net.URL
import javax.imageio.ImageIO

@Composable
actual fun PokemonImage(imageUrl: String) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(imageUrl) {
        runCatching {
            val url = URL(imageUrl)
            val bufferedImage = ImageIO.read(url)
            bufferedImage.toComposeImageBitmap()
        }.onSuccess {
            imageBitmap = it
        }
    }

    imageBitmap?.let {
        Image(
            bitmap = it,
            contentDescription = "Pokemon Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}
