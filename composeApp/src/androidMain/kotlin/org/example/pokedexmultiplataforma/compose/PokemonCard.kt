package org.example.pokedexmultiplataforma.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.example.pokedexmultiplataforma.api.PokemonUI

@Composable
actual fun PokemonCard(pokemon: PokemonUI) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color(0xFFFAFAFA)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pokedex-style header bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color(0xFFB71C1C)) // rojo pokedex
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre y número
            Text(
                text = "#${pokemon.number} ${pokemon.name.replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF263238),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Imagen del Pokémon
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFFB0BEC5), RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .shadow(8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Tipos del Pokémon
            Row(
                horizontalArrangement = Arrangement.spacedBy(35.dp),
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                pokemon.types.forEach { type ->
                    TypeChip(type.name, type.imageUrl)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción con estilo tipo "pantalla LCD"
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

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}



@Composable
fun TypeChip(typeName: String, iconUrl: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start=4.dp, end=4.dp)
    ) {
        AsyncImage(
            model = iconUrl,
            contentDescription = typeName,
            modifier = Modifier
                .size(80.dp) // Tamaño de layout visible
                .graphicsLayer(
                    scaleX = 1.5f, // Escalado visual
                    scaleY = 1.5f
                )
        )
    }
}