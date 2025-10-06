package com.example.pawdopt.data.model

data class AddPetFormState(
    val nombre: String = "",
    val especie: String = "",
    val edad: String = "",
    val raza: String = "",
    val descripcion: String = "",
    val ubicacion: String = "",
    val fotoUri: String? = null,

    val nombreError: String? = null,
    val especieError: String? = null,
    val edadError: String? = null,
    val razaError: String? = null,
    val descripcionError: String? = null,
    val ubicacionError: String? = null,

    val isValid: Boolean = false
)