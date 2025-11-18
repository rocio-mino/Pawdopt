package com.example.pawdopt.data.remote.repository

import com.example.pawdopt.data.model.Pet
import com.example.pawdopt.data.remote.RetrofitInstance
import com.example.pawdopt.data.remote.api.PetApiService

class PetRemoteRepository {

    private val api = RetrofitInstance.retrofit.create(PetApiService::class.java)

    suspend fun getAllPets(): List<Pet> = api.getAllPets()

    suspend fun addPet(pet: Pet): Pet = api.addPet(pet)

    suspend fun updatePet(id: Long, pet: Pet): Pet = api.updatePet(id, pet)

    suspend fun deletePet(id: Long) = api.deletePet(id)
}