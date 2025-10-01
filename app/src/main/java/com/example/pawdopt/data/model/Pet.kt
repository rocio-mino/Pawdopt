package com.example.pawdopt.data.model

data class Pet(
    val id: Int = 0,
    val nombre: String,
    val especie: String,
    val edad: Int,
    val raza: String,
    val descripcion: String,
    val fotoUri: String?,
    val ubicacion: String?,
    val userId: Int
)