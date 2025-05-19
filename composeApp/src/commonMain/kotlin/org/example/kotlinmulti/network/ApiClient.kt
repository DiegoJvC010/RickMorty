package org.example.kotlinmulti.network

import io.ktor.client.HttpClient //cliente http principal de ktor
import io.ktor.client.call.body //extension para obtener cuerpo de la respuesta
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation //plugin para negociar contenido json
import io.ktor.client.request.get //funcion para realizar peticiones GET
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json //adaptador json para ktor
import kotlinx.serialization.json.Json //configuracion de serializacion JSON
import kotlinx.serialization.json.jsonObject
import org.example.kotlinmulti.model.CharacterResponse //modelo de datos para la respuesta
import org.example.kotlinmulti.model.Info

//un singleton que se encarga de comunicarse con la API de Rick and Morty
object ApiClient {
    //inicializa el cliente http con soporte para JSON
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true //ignora campos extras en la respuesta
            })
        }
    }

    //fetchPage recupera una pagina de personajes con filtros opcionales
    suspend fun fetchPage(
        page: Int, //numero de pagina (inicia en 1)
        name: String? = null, //filtro por nombre (coincidencia parcial)
        status: String? = null //filtro por estado: "alive", "dead" o "unknown"
    ): CharacterResponse {
        //endpoint base de personajes
        val baseUrl = "https://rickandmortyapi.com/api/character"

        //arma la lista de parametros de query
        val queryParams = listOfNotNull(
            "page=$page", //pagina obligatoria
            name?.takeIf { it.isNotBlank() }?.let { "name=$it" }, //agrega filtro nombre si existe
            status?.let { "status=${it.lowercase()}" } //agrega filtro estado si existe
        )

        //une parametros con '&'
        val queryString = queryParams.joinToString("&")

        //construye la url completa
        val fullUrl = if (queryString.isNotEmpty()) "$baseUrl?$queryString" else baseUrl

        //se hace la peticion pero obteniendo el HttpResponse crudo
        val response: HttpResponse = client.get(fullUrl)

        //se lee el cuerpo como texto
        val text = response.bodyAsText()

        //se parsea con Json.parseToJsonElement
        val element = Json.parseToJsonElement(text).jsonObject

        //si viene {"error":"There is nothing here"} se devuelve vacio
        if (element["error"] != null) {
            return CharacterResponse(
                info = Info(count = 0, pages = 0, next = null, prev = null),
                results = emptyList()
            )
        }
        //si no, se deserializa de forma normal
        return Json { ignoreUnknownKeys = true }.decodeFromString(
            CharacterResponse.serializer(),
            text
        )
    }
}