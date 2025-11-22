package com.example.pawdopt.data.model

data class AdoptionRequest(
    val id: Long? = null,
    val adopterId: Long,
    val ownerId: Long,
    val petId: Long,
    val status: String = "Pendiente"
)