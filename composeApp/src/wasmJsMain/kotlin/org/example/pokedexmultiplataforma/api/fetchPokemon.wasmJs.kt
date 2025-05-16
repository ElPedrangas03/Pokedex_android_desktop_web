package org.example.pokedexmultiplataforma.api

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import kotlinx.browser.window
import org.example.pokedexmultiplataforma.api.PokemonUI
import com.example.pokedex.api.PokemonResponse
import com.example.pokedex.api.SpeciesResponse

val jsJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

val jsClient = HttpClient(Js) {
    install(ContentNegotiation) {
        json(jsJson)
    }
}

val typeNameToId = mapOf(
    "normal" to 1, "fighting" to 2, "flying" to 3, "poison" to 4, "ground" to 5,
    "rock" to 6, "bug" to 7, "ghost" to 8, "steel" to 9, "fire" to 10, "water" to 11,
    "grass" to 12, "electric" to 13, "psychic" to 14, "ice" to 15, "dragon" to 16,
    "dark" to 17, "fairy" to 18
)

actual suspend fun fetchPokemon(id: Int): PokemonUI = coroutineScope {
    try {
        val pokemonDeferred = async {
            val response = jsClient.get("https://pokeapi.co/api/v2/pokemon/$id")
            jsJson.decodeFromString<PokemonResponse>(response.bodyAsText())
        }

        val speciesDeferred = async {
            val response = jsClient.get("https://pokeapi.co/api/v2/pokemon-species/$id")
            jsJson.decodeFromString<SpeciesResponse>(response.bodyAsText())
        }

        val pokemonResponse = pokemonDeferred.await()
        val speciesResponse = speciesDeferred.await()

        val imageUrl = pokemonResponse.sprites.other["official-artwork"]?.frontDefault ?: ""

        val types = pokemonResponse.types.map { typeSlot ->
            val typeName = typeSlot.type.name
            val typeId = typeNameToId[typeName] ?: 0
            val iconUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/types/generation-viii/brilliant-diamond-and-shining-pearl/$typeId.png"
            TypeWithIcon(typeName, iconUrl)
        }

        val description = speciesResponse.flavorTextEntries.firstOrNull {
            it.language.name == "es"
        }?.flavorText
            ?.replace("\n", " ")
            ?.replace("\u000c", " ")
            ?: "No hay descripción disponible en español."

        PokemonUI(
            name = pokemonResponse.name,
            number = id,
            types = types,
            imageUrl = imageUrl,
            description = description
        )
    } catch (e: Exception) {
        e.printStackTrace()
        PokemonUI(
            name = "Desconocido",
            number = id,
            types = emptyList(),
            imageUrl = "",
            description = "No se pudo cargar la información del Pokémon."
        )
    }
}