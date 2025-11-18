package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawdopt.data.model.AdoptionRequest
import com.example.pawdopt.data.remote.repository.AdoptionRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdoptionState(
    val requests: List<AdoptionRequest> = emptyList()
)

class AdoptionViewModel(
    private val repo: AdoptionRemoteRepository = AdoptionRemoteRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(AdoptionState())
    val state: StateFlow<AdoptionState> = _state

    fun refreshAll() {
        viewModelScope.launch {
            val all = repo.getAll()
            _state.value = _state.value.copy(requests = all)
        }
    }

    fun createRequest(adopterId: Long, ownerId: Long, petId: Long) {
        viewModelScope.launch {
            val req = AdoptionRequest(
                id = 0,
                adopterId = adopterId,
                ownerId = ownerId,
                petId = petId,
                status = "Pendiente"
            )
            repo.create(req)
            refreshAll()
        }
    }

    fun acceptRequest(id: Long) {
        viewModelScope.launch {
            repo.accept(id)
            refreshAll()
        }
    }

    fun rejectRequest(id: Long) {
        viewModelScope.launch {
            repo.reject(id)
            refreshAll()
        }
    }
}