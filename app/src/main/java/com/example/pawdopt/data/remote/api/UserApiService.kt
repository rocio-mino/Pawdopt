package com.example.pawdopt.data.remote.api

import com.example.pawdopt.data.model.User
import retrofit2.http.*

interface UserApiService {

    // Obtener todos
    @GET("api/users")
    suspend fun getAll(): List<User>

    // Obtener por id
    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): User

    // Crear usuario (registro)
    @POST("api/users")
    suspend fun register(@Body user: User): User

    // Actualizar
    @PUT("api/users/{id}")
    suspend fun update(@Path("id") id: Long, @Body user: User): User

    // Login usando BODY JSON
    @POST("api/users/login")
    suspend fun login(@Body body: Map<String, String>): User?

    // Actualizar foto
    @PATCH("api/users/{id}/photo")
    suspend fun updatePhoto(
        @Path("id") id: Long,
        @Body body: Map<String, String>
    ): User
}