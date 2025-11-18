package com.example.pawdopt.data.remote.api

import com.example.pawdopt.data.model.AdoptionRequest
import retrofit2.http.*

interface AdoptionApiService {

    @GET("api/adoptions")
    suspend fun getAll(): List<AdoptionRequest>

    @GET("api/adoptions/adopter/{id}")
    suspend fun getByAdopter(@Path("id") id: Long): List<AdoptionRequest>

    @GET("api/adoptions/owner/{id}")
    suspend fun getByOwner(@Path("id") id: Long): List<AdoptionRequest>

    @POST("api/adoptions")
    suspend fun create(@Body req: AdoptionRequest): AdoptionRequest

    @PATCH("api/adoptions/{id}/accept")
    suspend fun accept(@Path("id") id: Long)

    @PATCH("api/adoptions/{id}/reject")
    suspend fun reject(@Path("id") id: Long)
}