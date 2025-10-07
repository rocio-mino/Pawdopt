package com.example.pawdopt.data.repository

import com.example.pawdopt.data.model.AdoptionRequest

class AdoptionRepository {
    private val requests = mutableListOf<AdoptionRequest>()
    private var nextId = 1

    fun insertRequest(request: AdoptionRequest): AdoptionRequest {
        val newRequest = request.copy(id = nextId++)
        requests.add(newRequest)
        return newRequest
    }

    fun getAllRequests(): List<AdoptionRequest> = requests.toList()

    fun getRequestsByAdopter(adopterId: Int): List<AdoptionRequest> =
        requests.filter { it.adopterId == adopterId }

    fun getRequestsByOwner(ownerId: Int): List<AdoptionRequest> =
        requests.filter { it.ownerId == ownerId }

    fun updateRequestStatus(requestId: Int, status: String) {
        val idx = requests.indexOfFirst { it.id == requestId }
        if (idx != -1) {
            requests[idx] = requests[idx].copy(status = status)
        }
    }

    fun deleteRequestsByPetId(petId: Int) {
        requests.removeIf { it.petId == petId }
    }
}
