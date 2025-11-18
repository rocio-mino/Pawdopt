package com.example.pawdopt.data.remote.repository

import com.example.pawdopt.data.model.AdoptionRequest
import com.example.pawdopt.data.remote.RetrofitInstance
import com.example.pawdopt.data.remote.api.AdoptionApiService

class AdoptionRemoteRepository {

    private val api = RetrofitInstance.retrofit.create(AdoptionApiService::class.java)

    suspend fun getAll() = api.getAll()

    suspend fun getByAdopter(id: Long) = api.getByAdopter(id)

    suspend fun getByOwner(id: Long) = api.getByOwner(id)

    suspend fun create(req: AdoptionRequest) = api.create(req)

    suspend fun accept(id: Long) = api.accept(id)

    suspend fun reject(id: Long) = api.reject(id)
}