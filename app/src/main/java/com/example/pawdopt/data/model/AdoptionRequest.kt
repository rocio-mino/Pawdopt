package com.example.pawdopt.data.model

data class AdoptionRequest(
    val id: Int = 0,
    val adopterId: Int,
    val ownerId: Int,
    val petId: Int,
    val status: String = "Pendiente"
)
