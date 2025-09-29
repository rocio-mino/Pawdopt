package com.example.pawdopt.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requests")
data class RequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int,
    val userId: Int,
    val estado: String,
    val contacto: String
)