package org.example.pokedexmultiplataforma.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.pokedexmultiplataforma.api.PokemonUI
import org.jetbrains.skia.Image as SkiaImage
import java.net.URL

@Composable
actual fun PokemonCard(pokemon: PokemonUI) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 500.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color(0xFFB71C1C)) // rojo pokedex
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "#${pokemon.number} ${pokemon.name.uppercase()}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF263238)
                )

                Spacer(modifier = Modifier.height(16.dp))

                val painter = rememberImagePainterSkia(pokemon.imageUrl)

                if (painter != null) {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .background(Color.LightGray, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Cargando imagen...", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    pokemon.types.forEach { type ->
                        val typePainter = rememberImagePainterSkia(type.imageUrl)

                        if (typePainter != null) {
                            Image(
                                painter = typePainter,
                                contentDescription = type.name,
                                modifier = Modifier.size(100.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.Gray, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    type.name.take(1).uppercase(),
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE0F7FA), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF0097A7), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = pokemon.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            fontSize = 16.sp,
                            color = Color(0xFF004D40),
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun rememberImagePainterSkia(url: String): Painter? {
    var painter by remember { mutableStateOf<Painter?>(null) }

    LaunchedEffect(url) {
        painter = withContext(Dispatchers.IO) {
            try {
                val stream = URL(url).openStream().readBytes()
                val image = SkiaImage.makeFromEncoded(stream)
                BitmapPainter(image.toComposeImageBitmap())
            } catch (e: Exception) {
                null
            }
        }
    }

    return painter
}