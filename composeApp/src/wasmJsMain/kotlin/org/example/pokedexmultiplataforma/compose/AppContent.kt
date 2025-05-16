package org.example.pokedexmultiplataforma.compose

import androidx.compose.runtime.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.attributes.*
import org.example.pokedexmultiplataforma.api.PokemonUI
import org.example.pokedexmultiplataforma.api.fetchPokemon

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

    Div({
        style {
            property("padding", "24px")
            property("max-width", "720px")
            property("margin", "0 auto")
            fontFamily("sans-serif")
            backgroundColor(Color.white)
        }
    }) {
        H1({
            style {
                fontSize(32.px)
                marginBottom(24.px)
                textAlign("center")
            }
        }) {
            Text("Pokédex")
        }

        Div({
            style {
                display(DisplayStyle.Flex)
                flexWrap(FlexWrap.Wrap)
                gap(12.px)
                marginBottom(16.px)
                justifyContent(JustifyContent.Center)
            }
        }) {
            Input(InputType.Number, attrs = {
                value(inputText)
                onInput { event -> inputText = event.value.toString() }
                attr("placeholder", "Número del Pokémon")
                style {
                    padding(10.px)
                    borderRadius(12.px)
                    fontSize(16.px)
                    border {
                        width(1.px)
                        color(Color("#B0BEC5"))
                        style(LineStyle.Solid)
                    }
                    backgroundColor(Color("#FAFAFA"))
                    property("box-shadow", "0px 2px 4px rgba(0,0,0,0.1)")
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
                            pokemon = fetchPokemon(number)
                            isLoading = false
                        }
                    }
                }
                style {
                    padding(10.px, 16.px)
                    borderRadius(12.px)
                    backgroundColor(Color("#1976D2"))
                    color(Color.white)
                    border {
                        style(LineStyle.None)
                    }
                    fontWeight("bold")
                    property("cursor", "pointer")
                    property("transition", "background-color 0.3s")
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

        pokemon?.let {
            if (isLoading) {
                P({
                    style {
                        textAlign("center")
                        marginBottom(16.px)
                        fontStyle("italic")
                    }
                }) {
                    Text("Cargando...")
                }
            }
            Div({
                style {
                    display(DisplayStyle.Flex)
                    flexWrap(FlexWrap.Wrap)
                    gap(12.px)
                    marginBottom(16.px)
                    justifyContent(JustifyContent.Center)
                }
            }){
                PokemonCard(pokemon = it)
            }
        }
        Div({
            style {
                display(DisplayStyle.Flex)
                flexWrap(FlexWrap.Wrap)
                gap(12.px)
                marginBottom(16.px)
                justifyContent(JustifyContent.Center)
            }
        }) {
            Button(attrs = {
                onClick {
                    scope.launch {
                        isLoading = true
                        pokemon = fetchPokemon((1..1000).random())
                        isLoading = false
                    }
                }
                style {
                    marginTop(24.px)
                    padding(10.px, 20.px)
                    borderRadius(12.px)
                    backgroundColor(Color("#43A047"))
                    color(Color.white)
                    fontWeight("bold")
                    border {
                        style(LineStyle.None)
                    }
                    property("cursor", "pointer")
                    property("transition", "background-color 0.3s")
                }
            }) {
                Text("Consultar Pokémon Aleatorio")
            }
        }
    }
}