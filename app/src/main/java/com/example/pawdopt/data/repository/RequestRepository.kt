package com.example.pawdopt.data.repository

import com.example.pawdopt.data.model.Request

class RequestRepository {
    private val requests = mutableListOf<Request>()
    fun getRequestsByUser(userId: Int): List<Request> =
        requests.filter { it.userId == userId }
    fun getRequestsByPet(petId: Int): List<Request> =
        requests.filter { it.petId == petId }
    fun insertRequest(request: Request) {
        requests.add(request.copy(id = requests.size + 1))
    }
    fun updateRequest(request: Request) {
        val index = requests.indexOfFirst { it.id == request.id }
        if (index != -1) requests[index] = request
    }
    fun deleteRequest(request: Request) {
        requests.removeIf { it.id == request.id }
    }
}