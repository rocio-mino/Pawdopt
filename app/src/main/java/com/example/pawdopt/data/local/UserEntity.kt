package com.example.pawdopt.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, //autoenerate para que el id se cree solo
    val nombre: String,
    val email: String,
    val password: String
)