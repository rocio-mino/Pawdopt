package com.example.pawdopt.data.model

data class Pet(
    val id: Int = 0,
    val nombre: String = "",
    val especie: String = "",
    val edad: Int = 0,
    val raza: String = "",
    val descripcion: String = "",
    val fotoUri: String? = null,
    val ubicacion: String = "",
    val ownerId: Int = 0
)