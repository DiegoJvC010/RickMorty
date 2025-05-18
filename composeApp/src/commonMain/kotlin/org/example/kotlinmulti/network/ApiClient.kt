package org.example.kotlinmulti.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.kotlinmulti.model.Character
import org.example.kotlinmulti.model.CharacterResponse

object ApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    /** Fetch a single page, con opcionales filtros */
    suspend fun fetchPage(
        page: Int,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null
    ): CharacterResponse {
        // Construir query string dinámico
        val base = "https://rickandmortyapi.com/api/character"
        val params = listOfNotNull(
            "page=$page",
            name?.let { "name=${it}" },
            status?.let { "status=${it.lowercase()}" },
            species?.let { "species=${it}" },
            type?.let { "type=${it}" },
            gender?.let { "gender=${it.lowercase()}" }
        ).joinToString("&")
        val url = "$base?$params"
        return client.get(url).body()
    }

    /** Recupera *todos* los personajes que cumplan el filtro */
    suspend fun fetchAllCharacters(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        type: String? = null,
        gender: String? = null
    ): List<Character> {
        // Primera página para saber cuántas vienen
        val first = fetchPage(1, name, status, species, type, gender)
        val acc = mutableListOf<Character>().apply { addAll(first.results) }
        // Si hay más páginas, recorrerlas en paralelo o secuencialmente
        for (p in 2..first.info.pages) {
            val resp = fetchPage(p, name, status, species, type, gender)
            acc += resp.results
        }
        return acc
    }
}
