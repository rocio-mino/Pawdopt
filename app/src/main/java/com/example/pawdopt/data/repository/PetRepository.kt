package com.example.pawdopt.data.repository

import com.example.pawdopt.data.local.PetDao
import com.example.pawdopt.data.local.PetEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetRepository @Inject constructor(
    private val petDao: PetDao
) {
    suspend fun insertPet(pet: PetEntity) = petDao.insertPet(pet)
    suspend fun updatePet(pet: PetEntity) = petDao.updatePet(pet)
    suspend fun deletePet(pet: PetEntity) = petDao.deletePet(pet)
    fun getAllPets(): Flow<List<PetEntity>> = petDao.getAllPets()

    fun getPetById(id: Int): Flow<PetEntity?> = petDao.getPetById(id)
}