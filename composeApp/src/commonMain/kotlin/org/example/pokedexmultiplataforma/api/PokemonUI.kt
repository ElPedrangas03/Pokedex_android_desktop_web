package org.example.pokedexmultiplataforma.api

data class PokemonUI(
    val name: String,
    val number: Int,
    val imageUrl: String,
    val types: List<TypeWithIcon>,
    val description: String
)

data class TypeWithIcon(
    val name: String,
    val imageUrl: String
)