package org.example.pokedexmultiplataforma.compose

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import org.example.pokedexmultiplataforma.api.PokemonUI
import org.example.pokedexmultiplataforma.api.fetchPokemon
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

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

    Div({
        style {
            position(Position.Absolute)
            top(0.px)
            left(0.px)
            right(0.px)
            bottom(0.px)
            overflowY("auto")
            padding(0.px)
            margin(0.px)
            backgroundColor(Color("#FAFAFA"))
        }
    }) {
        Div({
            style {
                maxWidth(720.px)
                width(100.percent)
                padding(24.px)
                fontFamily("sans-serif")
                boxSizing("border-box")
            }
        }) {
            H1({
                style {
                    fontSize(32.px)
                    marginBottom(24.px)
                    textAlign("center")
                }
            }) {
                Text("Pokédex Web")
            }

            Div({
                style {
                    display(DisplayStyle.Flex)
                    flexWrap(FlexWrap.Wrap)
                    gap(12.px)
                    justifyContent(JustifyContent.Center)
                    marginBottom(16.px)
                }
            }) {
                Input(InputType.Number, attrs = {
                    value(inputText)
                    onInput { inputText = it.value.toString() }
                    attr("placeholder", "Número del Pokémon")
                    style {
                        padding(10.px)
                        fontSize(16.px)
                        borderRadius(12.px)
                        border {
                            style(LineStyle.Solid)
                            width(1.px)
                            color(Color("#B0BEC5"))
                        }
                        backgroundColor(Color.white)
                        property("box-shadow", "0px 2px 4px rgba(0,0,0,0.1)")
                        width(200.px)
                    }
                })

                Button(attrs = {
                    onClick {
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
                    }
                    style {
                        padding(10.px, 16.px)
                        borderRadius(12.px)
                        backgroundColor(Color("#1976D2"))
                        color(Color.white)
                        fontWeight("bold")
                        border {
                            style(LineStyle.None)
                        }
                        property("cursor", "pointer")
                    }
                }) {
                    Text("Buscar")
                }
            }

            errorText?.let {
                P({
                    style {
                        color(Color.red)
                        textAlign("center")
                        marginBottom(16.px)
                    }
                }) {
                    Text(it)
                }
            }

            if (isLoading) {
                P({
                    style {
                        textAlign("center")
                        fontStyle("italic")
                        marginBottom(12.px)
                    }
                }) {
                    Text("Cargando...")
                }
            }

            pokemon?.let {
                PokemonCardWasm(it)

                Div({
                    style {
                        display(DisplayStyle.Flex)
                        justifyContent(JustifyContent.Center)
                        marginTop(24.px)
                        marginBottom(24.px)
                    }
                }) {
                    Button(attrs = {
                        onClick {
                            scope.launch {
                                isLoading = true
                                val result = fetchPokemon((1..1000).random())
                                pokemon = result
                                if (consultedList.none { it.number == result.number }) {
                                    consultedList.add(result)
                                }
                                isLoading = false
                            }
                        }
                        style {
                            padding(10.px, 20.px)
                            borderRadius(12.px)
                            backgroundColor(Color("#43A047"))
                            color(Color.white)
                            fontWeight("bold")
                            border {
                                style(LineStyle.None)
                            }
                            property("cursor", "pointer")
                        }
                    }) {
                        Text("Consultar Pokémon Aleatorio")
                    }
                }
            }

            if (consultedList.isNotEmpty()) {
                H3 {
                    Text("Pokémon consultados")
                }

                consultedList.forEach {
                    Div({
                        style {
                            backgroundColor(Color("#F5F5F5"))
                            borderRadius(12.px)
                            padding(12.px)
                            marginBottom(8.px)
                            display(DisplayStyle.Flex)
                            alignItems(AlignItems.Center)
                            gap(12.px)
                        }
                    }) {
                        Img(src = it.imageUrl, attrs = {
                            style {
                                width(64.px)
                                height(64.px)
                            }
                        })
                        Text("#${it.number} ${it.name.uppercase()}")
                    }
                }
            }
        }
    }
}
