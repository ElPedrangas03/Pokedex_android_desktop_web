package org.example.pokedexmultiplataforma.compose

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import org.example.pokedexmultiplataforma.api.PokemonUI
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
actual fun PokemonCard(pokemon: PokemonUI) {
    Div({
        style {
            property("width", "100%")
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.Center)
        }
    }) {
        Div({
            style {
                property("max-width", "500px")
                property("background-color", "#FAFAFA")
                borderRadius(16.px)
                padding(16.px)
                property("box-shadow", "0px 4px 12px rgba(0,0,0,0.1)")
                fontFamily("sans-serif")
            }
        }) {
            // Pokedex-style header bar
            Div({
                style {
                    height(8.px)
                    backgroundColor(Color("#B71C1C"))
                    property("border-top-left-radius", "12px")
                    property("border-top-right-radius", "12px")
                    marginBottom(16.px)
                }
            })

            // Nombre y número
            H3({
                style {
                    color(Color("#263238"))
                    fontSize(24.px)
                    fontWeight("bold")
                    textAlign("center")
                    marginBottom(12.px)
                }
            }) {
                Text("#${pokemon.number} ${pokemon.name.replaceFirstChar { it.uppercase() }}")
            }

            // Imagen del Pokémon
            RemoteImage(pokemon.imageUrl, size = 220)

            // Tipos del Pokémon
            Div({
                style {
                    display(DisplayStyle.Flex)
                    justifyContent(JustifyContent.Center)
                    gap(24.px)
                    padding(16.px)
                }
            }) {
                pokemon.types.forEach { type ->
                    RemoteImage(type.imageUrl, size = 100)
                }
            }

            // Descripción
            Div({
                style {
                    backgroundColor(Color("#E0F7FA"))
                    border {
                        width(1.px)
                        style(LineStyle.Solid)
                        color(Color("#0097A7"))
                    }
                    borderRadius(12.px)
                    padding(12.px)
                    fontSize(16.px)
                    color(Color("#004D40"))
                    fontStyle("italic")
                    lineHeight("1.5")
                }
            }) {
                Text(pokemon.description)
            }
        }
    }
}

@Composable
fun RemoteImage(url: String, size: Int) {
    Div({
        style {
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.Center)
            alignItems(AlignItems.Center)
            marginBottom(16.px)
        }
    }) {
        Img(src = url, attrs = {
            attr("alt", "image")
            style {
                width(size.px)
                height(size.px)
                property("object-fit", "contain")
                borderRadius(12.px)
                border {
                    width(2.px)
                    style(LineStyle.Solid)
                    color(Color("#B0BEC5"))
                }
                backgroundColor(Color("#FFFFFF"))
                property("box-shadow", "0 2px 6px rgba(0,0,0,0.15)")
            }
        })
    }
}

