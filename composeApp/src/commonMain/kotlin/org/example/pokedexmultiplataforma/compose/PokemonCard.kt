package org.example.pokedexmultiplataforma.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.example.pokedexmultiplataforma.api.PokemonUI

@Composable
fun PokemonCard(pokemon: PokemonUI) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "#${pokemon.number} ${pokemon.name.uppercase()}",
                fontSize = 20.sp
            )

            Spacer(Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F0F0) // gris claro
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    KamelImage(
                        resource = asyncPainterResource(pokemon.imageUrl),
                        contentDescription = pokemon.name,
                        modifier = Modifier.size(180.dp),
                        onLoading = {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        },
                        onFailure = {
                            Text("❌", modifier = Modifier.align(Alignment.Center))
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                pokemon.types.forEach { type ->
                    KamelImage(
                        resource = asyncPainterResource(type.imageUrl),
                        contentDescription = type.name,
                        modifier = Modifier.size(120.dp),
                        onLoading = {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        },
                        onFailure = {
                            Text("❌", modifier = Modifier.align(Alignment.Center))
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Descripción
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
