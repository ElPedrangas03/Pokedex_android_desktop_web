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
actual fun PokemonImage(imageUrl: String) {
    Div({
        style {
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.Center)
            alignItems(AlignItems.Center)
            marginBottom(16.px)
        }
    }) {
        Img(src = imageUrl, attrs = {
            attr("alt", "image")
            style {
                width(200.px)
                height(200.px)
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