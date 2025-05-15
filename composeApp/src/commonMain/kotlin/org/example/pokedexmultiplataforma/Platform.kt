package org.example.pokedexmultiplataforma

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform