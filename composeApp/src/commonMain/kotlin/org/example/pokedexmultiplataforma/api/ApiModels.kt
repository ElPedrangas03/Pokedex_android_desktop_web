package com.example.pokedex.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonResponse(
    val name: String,
    val order: Int,
    val types: List<TypeSlot>,
    val sprites: Sprites
)

@Serializable
data class TypeSlot(
    val slot: Int,
    val type: NamedAPIResource
)

@Serializable
data class Sprites(
    val other: Map<String, OtherSprite> = emptyMap()
)

@Serializable
data class OtherSprite(
    @SerialName("front_default") val frontDefault: String? = null
)

@Serializable
data class SpeciesResponse(
    @SerialName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>
)

@Serializable
data class FlavorTextEntry(
    @SerialName("flavor_text") val flavorText: String,
    val language: NamedAPIResource,
    val version: NamedAPIResource
)

@Serializable
data class Language(val name: String)
@Serializable
data class Version(val name: String)

@Serializable
data class TypeDetailResponse(
    val sprites: TypeSprites
)

@Serializable
data class TypeSprites(
    @SerialName("generation-iv") val generationIv: GenerationIv
)

@Serializable
data class GenerationIv(
    val platinum: PlatinumSprites
)

@Serializable
data class PlatinumSprites(
    @SerialName("name_icon") val nameIcon: String
)

@Serializable
data class NamedAPIResource(
    val name: String,
    val url: String
)