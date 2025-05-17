package org.example.pokedexmultiplataforma.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.pokedexmultiplataforma.api.PokemonUI

@Composable
fun PokemonCard(pokemon: PokemonUI) {
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

                val shape = RoundedCornerShape(8.dp)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp)) // bordes redondeados
                        .background(Color.Gray.copy(alpha = 0.3f)) // gris con transparencia para suavizar
                        .border(
                            width = 1.dp,
                            color = Color.Gray.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(12.dp),
                            clip = false
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    PokemonImage(pokemon.imageUrl)
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
                        PokemonTypeItem(type.name, type.imageUrl)
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
fun PokemonTypeItem(typeName: String, imageUrl: String) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(shape),
        contentAlignment = Alignment.Center
    ) {
        // Aquí puedes usar la imagen del tipo con la imagen multiplataforma
        PokemonImage(imageUrl)
        // O si quieres, si la imagen falla, mostrar la primera letra
        // Text(typeName.take(1).uppercase(), fontSize = 12.sp, color = Color.White)
    }
}