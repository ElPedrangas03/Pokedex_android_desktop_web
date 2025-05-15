package org.example.pokedexmultiplataforma.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import com.example.pokedex.api.PokemonResponse
import com.example.pokedex.api.SpeciesResponse

val desktopJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

val desktopClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(desktopJson)
    }
    defaultRequest {
        headers.append("Accept", "application/json")
    }
    engine {
        requestTimeout = 100_000
    }
}

actual suspend fun fetchPokemon(id: Int): PokemonUI = coroutineScope {
    try {
        val pokemonDeferred = async {
            val jsonRaw = getValidatedJson("https://pokeapi.co/api/v2/pokemon/$id")
            desktopJson.decodeFromString<PokemonResponse>(jsonRaw)
        }

        val speciesDeferred = async {
            val jsonRaw = getValidatedJson("https://pokeapi.co/api/v2/pokemon-species/$id")
            desktopJson.decodeFromString<SpeciesResponse>(jsonRaw)
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

private val typeNameToId = mapOf(
    "normal" to 1, "fighting" to 2, "flying" to 3, "poison" to 4, "ground" to 5,
    "rock" to 6, "bug" to 7, "ghost" to 8, "steel" to 9, "fire" to 10, "water" to 11,
    "grass" to 12, "electric" to 13, "psychic" to 14, "ice" to 15, "dragon" to 16,
    "dark" to 17, "fairy" to 18
)

private suspend fun getValidatedJson(url: String, retries: Int = 3): String {
    repeat(retries) {
        val response = desktopClient.get(url)
        if (!response.status.isSuccess()) throw Exception("HTTP ${response.status.value} en $url")

        val raw = response.bodyAsText()
        if (raw.isBlank()) {
            delay(300L)
            return@repeat
        }

        val cleaned = raw
            .replace("\u000c", " ")
            .replace("\n", " ")
            .replace(Regex("[^\\x09\\x0A\\x0D\\x20-\\x7EáéíóúÁÉÍÓÚñÑüÜ¿¡.,:;()\\[\\]\\-\"']"), " ")

        try {
            desktopJson.parseToJsonElement(cleaned)
            return cleaned
        } catch (_: Exception) {
            delay(300L)
        }
    }

    throw Exception("No se pudo obtener JSON válido desde $url después de $retries intentos")
}