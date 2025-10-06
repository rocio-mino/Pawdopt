package com.example.pawdopt.data.model

data class User (
    val id: Int = 0,
    val nombre: String,
    val email: String,
    val password: String,
    val fotoUri: String? = null
)