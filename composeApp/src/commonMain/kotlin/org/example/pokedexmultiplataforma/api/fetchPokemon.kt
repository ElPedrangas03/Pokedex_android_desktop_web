package org.example.pokedexmultiplataforma.api

import org.example.pokedexmultiplataforma.api.PokemonUI

expect suspend fun fetchPokemon(id: Int): PokemonUI