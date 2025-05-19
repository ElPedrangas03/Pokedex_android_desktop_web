package org.example.pokedexmultiplataforma.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.pokedexmultiplataforma.api.fetchPokemonSimple
import org.example.pokedexmultiplataforma.api.PokemonUI

@Composable
fun AppContent() {
    val scope = rememberCoroutineScope()
    var pokemon by remember { mutableStateOf<PokemonUI?>(null) }
    var inputText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val consultedList = remember { mutableStateListOf<PokemonUI>() }

    LaunchedEffect(Unit) {
        isLoading = true
        val result = fetchPokemonSimple((1..1000).random())
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
                    Text("Pokédex", fontSize = 28.sp)
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
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            )
                        )

                        Button(onClick = {
                            val number = inputText.toIntOrNull()
                            if (number == null || number !in 1..1000) {
                                errorText = "Número inválido (1 - 1000)"
                            } else {
                                errorText = null
                                scope.launch {
                                    isLoading = true
                                    val result = fetchPokemonSimple(number)
                                    pokemon = result
                                    if (consultedList.none { it.number == result.number }) {
                                        consultedList.add(result)
                                    }
                                    isLoading = false
                                    inputText = ""
                                }
                            }
                        }) {
                            Text("Buscar")
                        }
                    }
                }

                errorText?.let {
                    item { Text(it, color = MaterialTheme.colorScheme.error) }
                }

                if (isLoading) {
                    item { CircularProgressIndicator() }
                }

                pokemon?.let {
                    item { PokemonCard(it) }
                }

                item {
                    Button(onClick = {
                        scope.launch {
                            isLoading = true
                            val result = fetchPokemonSimple((1..1000).random())
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
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                if (consultedList.isNotEmpty()) {
                    item {
                        Text(
                            "Pokémon consultados",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    items(consultedList) { pkm ->
                        PokemonCard(pkm)
                    }
                }
            }
        }
    }
}
