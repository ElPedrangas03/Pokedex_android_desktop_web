package org.example.pokedexmultiplataforma.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import kotlinx.serialization.decodeFromString
import org.example.pokedexmultiplataforma.api.*

val commonJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

val commonClient = HttpClient {
    install(ContentNegotiation) {
        json(commonJson)
    }
}

suspend fun fetchPokemonSimple(id: Int): PokemonUI {
    // Petición principal
    val baseUrl = "https://pokeapi.co/api/v2/pokemon/$id"
    val response: String = commonClient.get(baseUrl).bodyAsText()
    val json = commonJson.parseToJsonElement(response).jsonObject

    val name = json["name"]?.jsonPrimitive?.content ?: "Desconocido"
    val imageUrl = json["sprites"]!!
        .jsonObject["other"]!!
        .jsonObject["official-artwork"]!!
        .jsonObject["front_default"]!!
        .jsonPrimitive.content

    val types = json["types"]!!.jsonArray.map {
        val typeName = it.jsonObject["type"]!!
            .jsonObject["name"]!!
            .jsonPrimitive.content

        val iconUrl = typeNameToId[typeName]?.let { id ->
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/types/generation-viii/brilliant-diamond-and-shining-pearl/$id.png"
        } ?: ""

        TypeWithIcon(name = typeName, imageUrl = iconUrl)
    }

    // Petición para la descripción
    val speciesUrl = "https://pokeapi.co/api/v2/pokemon-species/$id"
    val speciesResponse = commonClient.get(speciesUrl).bodyAsText()
    val speciesJson = commonJson.parseToJsonElement(speciesResponse).jsonObject

    val description = speciesJson["flavor_text_entries"]
        ?.jsonArray
        ?.firstOrNull {
            it.jsonObject["language"]?.jsonObject?.get("name")?.jsonPrimitive?.content == "es"
        }?.jsonObject?.get("flavor_text")?.jsonPrimitive?.content
        ?.replace("\n", " ")
        ?.replace("\u000c", " ")
        ?: "No hay descripción disponible en español."

    return PokemonUI(
        name = name,
        number = id,
        imageUrl = imageUrl,
        types = types,
        description = description
    )
}

private val typeNameToId = mapOf(
    "normal" to 1, "fighting" to 2, "flying" to 3, "poison" to 4, "ground" to 5,
    "rock" to 6, "bug" to 7, "ghost" to 8, "steel" to 9, "fire" to 10, "water" to 11,
    "grass" to 12, "electric" to 13, "psychic" to 14, "ice" to 15, "dragon" to 16,
    "dark" to 17, "fairy" to 18
)
