package org.example.pokedexmultiplataforma.api

import com.example.pokedex.api.fetchPokemon

actual suspend fun fetchPokemon(id: Int): PokemonUI {
    return fetchPokemon(id)
}