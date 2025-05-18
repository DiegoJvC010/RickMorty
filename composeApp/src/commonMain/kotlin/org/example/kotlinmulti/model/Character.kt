package org.example.kotlinmulti.model

import kotlinx.serialization.*

@Serializable
//Data class que representa la respuesta de la API con pagina y lista de personajes
data class CharacterResponse(
    val info: Info,            //Informacion de paginacion (total, paginas, next, prev)
    val results: List<Character> //Lista de personajes de la pagina
)

@Serializable
//Data class con los datos de paginacion de la API
data class Info(
    val count: Int,            //Total de elementos en la coleccion
    val pages: Int,            //Total de paginas disponibles
    val next: String? = null,  //URL de la pagina siguiente (o null)
    val prev: String? = null   //URL de la pagina anterior (o null)
)

@Serializable
//Data class que modela un personaje
data class Character(
    val id: Int,               //Identificador unico del personaje
    val name: String,          //Nombre del personaje
    val status: String,        //Estado: Alive, Dead o unknown
    val species: String,       //Especie del personaje
    val image: String,         //URL de la imagen del personaje
    val origin: LocationRef,   //Referencia al lugar de origen
    val location: LocationRef, //Referencia a la ultima ubicacion conocida
    val type: String = "",     //Tipo o subtipo (puede estar vacio)
    val gender: String = "",   //Genero: Male, Female, etc. (puede estar vacio)
    val episode: List<String> = emptyList() //Lista de URLs de episodios
)

@Serializable
//Data class que referencia un lugar (origin o location)
data class LocationRef(
    val name: String,          //Nombre del lugar
    val url: String = ""       //URL del endpoint del lugar (puede estar vacia)
)
