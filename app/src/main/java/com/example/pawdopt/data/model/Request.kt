package com.example.pawdopt.data.model

data class Request(
    val id: Int = 0,
    val petId: Int,
    val adopterId: Int,
    val ownerId: Int,
    val estado: String,
    val contacto: String
)