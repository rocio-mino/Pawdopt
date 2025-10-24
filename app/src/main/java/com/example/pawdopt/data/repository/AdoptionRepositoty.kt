package com.example.pawdopt.data.repository

import com.example.pawdopt.data.model.AdoptionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AdoptionRepository {

    private val requests = mutableListOf<AdoptionRequest>()
    private var nextId = 1

    suspend fun insertRequest(request: AdoptionRequest): AdoptionRequest = withContext(Dispatchers.IO) {
        val newRequest = request.copy(id = nextId++)
        requests.add(newRequest)
        newRequest
    }

    suspend fun getAllRequests(): List<AdoptionRequest> = withContext(Dispatchers.IO) {
        requests.toList()
    }

    suspend fun getRequestsByAdopter(adopterId: Int): List<AdoptionRequest> = withContext(Dispatchers.IO) {
        requests.filter { it.adopterId == adopterId }
    }

    suspend fun getRequestsByOwner(ownerId: Int): List<AdoptionRequest> = withContext(Dispatchers.IO) {
        requests.filter { it.ownerId == ownerId }
    }

    suspend fun updateRequestStatus(requestId: Int, status: String) = withContext(Dispatchers.IO) {
        val idx = requests.indexOfFirst { it.id == requestId }
        if (idx != -1) {
            requests[idx] = requests[idx].copy(status = status)
        }
    }

    suspend fun deleteRequestsByPetId(petId: Int) = withContext(Dispatchers.IO) {
        requests.removeIf { it.petId == petId }
    }

    suspend fun deleteRequestById(requestId: Int) = withContext(Dispatchers.IO) {
        requests.removeIf { it.id == requestId }
    }
}
