package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawdopt.data.model.AdoptionRequest
import com.example.pawdopt.data.repository.AdoptionRepository
import com.example.pawdopt.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdoptionState(
    val requests: List<AdoptionRequest> = emptyList()
)

class AdoptionViewModel(
    private val repository: AdoptionRepository = AdoptionRepository(),
    private val petRepository: PetRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdoptionState())
    val state: StateFlow<AdoptionState> = _state.asStateFlow()

    init {
        refreshAll()
    }

    fun createRequest(adopterId: Int, ownerId: Int, petId: Int) {
        viewModelScope.launch {
            val request = AdoptionRequest(adopterId = adopterId, ownerId = ownerId, petId = petId)
            repository.insertRequest(request)
            refreshAll()
        }
    }

    fun refreshAll() {
        viewModelScope.launch {
            _state.value = _state.value.copy(requests = repository.getAllRequests())
        }
    }

    fun getRequestsForUser(userId: Int) {
        viewModelScope.launch {
            val sent = repository.getRequestsByAdopter(userId)
            val rec = repository.getRequestsByOwner(userId)
            _state.value = _state.value.copy(requests = sent + rec)
        }
    }

    fun acceptRequest(requestId: Int) {
        viewModelScope.launch {
            val req = repository.getAllRequests().find { it.id == requestId } ?: return@launch

            // Cambiamos el estado a "Aceptada"
            repository.updateRequestStatus(requestId, "Aceptada")

            petRepository.getPetById(req.petId)?.let {
                petRepository.deletePet(it)
            }

            refreshAll()
        }
    }

    fun rejectRequest(requestId: Int) {
        viewModelScope.launch {
            val req = repository.getAllRequests().find { it.id == requestId } ?: return@launch

            // Cambiamos el estado a "Rechazada"
            repository.updateRequestStatus(requestId, "Rechazada")

            repository.deleteRequestById(requestId)

            refreshAll()
        }
    }
}