package com.example.pawdopt.model

data class Request(
    val id: Int = 0,
    val petId: Int,
    val userId: Int,
    val estado: String,
    val contacto: String
)