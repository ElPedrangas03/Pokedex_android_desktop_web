package org.example.pokedexmultiplataforma.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import org.example.pokedexmultiplataforma.api.PokemonUI
import org.example.pokedexmultiplataforma.api.fetchPokemon

@Composable
actual fun AppContent() {
    val scope = rememberCoroutineScope()
    var pokemon by remember { mutableStateOf<PokemonUI?>(null) }
    var inputText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val consultedList = remember { mutableStateListOf<PokemonUI>() }

    LaunchedEffect(Unit) {
        isLoading = true
        val result = fetchPokemon((1..1000).random())
        pokemon = result
        consultedList.add(result)
        isLoading = false
    }

    MaterialTheme {
        Scaffold { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Pokédex",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            label = { Text("Número del Pokémon") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Button(onClick = {
                            val number = inputText.toIntOrNull()
                            if (number == null || number !in 1..1000) {
                                errorText = "Número inválido (1 - 1000)"
                            } else {
                                errorText = null
                                scope.launch {
                                    isLoading = true
                                    val result = fetchPokemon(number)
                                    pokemon = result
                                    if (consultedList.none { it.number == result.number }) {
                                        consultedList.add(result)
                                    }
                                    isLoading = false
                                }
                            }
                        }) {
                            Text("Buscar")
                        }
                    }
                }

                errorText?.let {
                    item {
                        Text(it, color = Color.Red)
                    }
                }

                item {
                    if (isLoading) {
                        CircularProgressIndicator()
                    }
                }

                pokemon?.let {
                    item {
                        PokemonCard(pokemon = it)
                    }
                }

                item {
                    Button(onClick = {
                        scope.launch {
                            isLoading = true
                            val result = fetchPokemon((1..1000).random())
                            pokemon = result
                            if (consultedList.none { it.number == result.number }) {
                                consultedList.add(result)
                            }
                            isLoading = false
                        }
                    }) {
                        Text("Consultar Pokémon Aleatorio")
                    }
                }

                item {
                    Text(
                        "Pokémon consultados",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                items(consultedList) { pkm ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Image(
                                painter = rememberImagePainter(pkm.imageUrl),
                                contentDescription = pkm.name,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "#${pkm.number} ${pkm.name.uppercase()}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
