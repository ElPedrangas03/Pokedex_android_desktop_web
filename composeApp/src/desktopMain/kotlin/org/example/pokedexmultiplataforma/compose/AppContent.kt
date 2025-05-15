package org.example.pokedexmultiplataforma.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.pokedexmultiplataforma.api.fetchPokemon
import org.example.pokedexmultiplataforma.api.PokemonUI
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun AppContent() {
    val scope = rememberCoroutineScope()
    var pokemon by remember { mutableStateOf<PokemonUI?>(null) }
    var inputText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true
        pokemon = fetchPokemon((1..1000).random())
        isLoading = false
    }

    MaterialTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pokédex",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

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
                                pokemon = fetchPokemon(number)
                                isLoading = false
                            }
                        }
                    }) {
                        Text("Buscar")
                    }
                }

                errorText?.let {
                    Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                pokemon?.let {
                    Box {
                        PokemonCard(pokemon = it)

                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color(0xAAFFFFFF))
                                    .zIndex(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = {
                        scope.launch {
                            isLoading = true
                            pokemon = fetchPokemon((1..1000).random())
                            isLoading = false
                        }
                    }) {
                        Text("Consultar Pokémon Aleatorio")
                    }
                }
            }
        }
    }
}