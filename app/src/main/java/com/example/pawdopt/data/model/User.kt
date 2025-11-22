package com.example.pawdopt.data.model

data class User(
    val id: Long? = null,
    val nombre: String,
    val email: String,
    val password: String,
    val fotoUri: String? = null
)
