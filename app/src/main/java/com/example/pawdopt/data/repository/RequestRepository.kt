package com.example.pawdopt.data.repository

import com.example.pawdopt.data.local.RequestDao
import com.example.pawdopt.data.local.RequestEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RequestRepository @Inject constructor(
    private val requestDao: RequestDao
) {
    suspend fun insertRequest(request: RequestEntity) = requestDao.insertRequest(request)

    suspend fun updateRequest(request: RequestEntity) = requestDao.updateRequest(request)

    suspend fun deleteRequest(request: RequestEntity) = requestDao.deleteRequest(request)

    suspend fun getRequestsByUser(userId: Int): List<RequestEntity> = requestDao.getRequestsByUser(userId)

    suspend fun getRequestsByPet(petId: Int): List<RequestEntity> = requestDao.getRequestsByPet(petId)
}