package com.example.pawdopt.data.model

data class AdoptionRequest(
    val id: Long = 0,
    val adopterId: Long,
    val ownerId: Long,
    val petId: Long,
    val status: String = "Pendiente"
)
