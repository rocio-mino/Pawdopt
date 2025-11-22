package com.example.pawdopt.data.model

import androidx.room.Ignore

data class Pet(
    val id: Long? = null,
    val nombre: String = "",
    val especie: String = "",
    val edad: Int = 0,
    val raza: String = "",
    val descripcion: String = "",
    val fotoUri: String? = null,
    val ubicacion: String = "",
    val ownerId: Long? = null
)