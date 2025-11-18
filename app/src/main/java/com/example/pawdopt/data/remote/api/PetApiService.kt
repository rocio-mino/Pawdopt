package com.example.pawdopt.data.remote.api

import com.example.pawdopt.data.model.Pet
import retrofit2.http.*

interface PetApiService {

    @GET("api/pets")
    suspend fun getAllPets(): List<Pet>

    @GET("api/pets/{id}")
    suspend fun getPet(@Path("id") id: Long): Pet

    @POST("api/pets")
    suspend fun addPet(@Body pet: Pet): Pet

    @PUT("api/pets/{id}")
    suspend fun updatePet(@Path("id") id: Long, @Body pet: Pet): Pet

    @DELETE("api/pets/{id}")
    suspend fun deletePet(@Path("id") id: Long)
}