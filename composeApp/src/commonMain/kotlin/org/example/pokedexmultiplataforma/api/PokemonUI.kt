package org.example.pokedexmultiplataforma.api

data class PokemonUI(
    val name: String,
    val number: Int,
    val types: List<TypeWithIcon>,
    val imageUrl: String,
    val description: String
)

data class TypeWithIcon(
    val name: String,
    val imageUrl: String
)