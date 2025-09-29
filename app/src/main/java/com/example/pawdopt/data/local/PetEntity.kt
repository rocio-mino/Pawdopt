package com.example.pawdopt.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val especie: String,
    val edad: Int,
    val raza: String,
    val descripcion: String,
    val fotoUri: String?,
    val ubicacion: String?,
    val userId: Int
)