package com.example.pawdopt.data.remote.repository

import com.example.pawdopt.data.model.User
import com.example.pawdopt.data.remote.RetrofitInstance
import com.example.pawdopt.data.remote.api.UserApiService

class UserRemoteRepository {

    private val api = RetrofitInstance.retrofit.create(UserApiService::class.java)

    suspend fun register(user: User): User =
        api.register(user)

    suspend fun login(email: String, password: String): User? =
        api.login(
            mapOf(
                "email" to email,
                "password" to password
            )
        )

    suspend fun getUser(id: Long): User =
        api.getUserById(id)

    suspend fun updatePhoto(id: Long, url: String): User =
        api.updatePhoto(id, mapOf("fotoUri" to url))
}