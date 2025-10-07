package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pawdopt.data.model.AdoptionRequest
import com.example.pawdopt.data.repository.AdoptionRepository
import com.example.pawdopt.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AdoptionState(
    val requests: List<AdoptionRequest> = emptyList()
)

class AdoptionViewModel(
    private val repository: AdoptionRepository = AdoptionRepository(),
    private val petRepository: PetRepository = PetRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(AdoptionState())
    val state: StateFlow<AdoptionState> = _state.asStateFlow()

    fun createRequest(adopterId: Int, ownerId: Int, petId: Int) {
        val request = AdoptionRequest(adopterId = adopterId, ownerId = ownerId, petId = petId)
        repository.insertRequest(request)
        _state.value = _state.value.copy(requests = repository.getAllRequests())
    }

    fun refreshAll() {
        _state.value = _state.value.copy(requests = repository.getAllRequests())
    }

    fun getRequestsForUser(userId: Int) {
        val sent = repository.getRequestsByAdopter(userId)
        val rec = repository.getRequestsByOwner(userId)
        _state.value = _state.value.copy(requests = (sent + rec))
    }

    fun acceptRequest(requestId: Int) {
        val req = repository.getAllRequests().find { it.id == requestId } ?: return
        repository.updateRequestStatus(requestId, "Aceptada")
        // eliminar mascota del repositorio
        petRepository.getPetById(req.petId)?.let {
            petRepository.deletePet(it)
        }
        // eliminar otras solicitudes asociadas a esa mascota
        repository.deleteRequestsByPetId(req.petId)
        _state.value = _state.value.copy(requests = repository.getAllRequests())
    }

    fun rejectRequest(requestId: Int) {
        repository.updateRequestStatus(requestId, "Rechazada")
        _state.value = _state.value.copy(requests = repository.getAllRequests())
    }
}
