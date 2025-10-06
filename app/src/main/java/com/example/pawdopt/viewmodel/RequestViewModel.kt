package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pawdopt.data.model.Request
import com.example.pawdopt.data.repository.RequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class RequestState(
    val requests: List<Request> = emptyList(),
    val selectedRequest: Request? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class RequestViewModel(
    private val requestRepository: RequestRepository = RequestRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(RequestState())
    val state: StateFlow<RequestState> = _state.asStateFlow()

    fun getRequestsByUser(userId: Int) {
        _state.value = _state.value.copy(
            requests = requestRepository.getRequestsByUser(userId)
        )
    }

    fun getRequestsByPet(petId: Int) {
        _state.value = _state.value.copy(
            requests = requestRepository.getRequestsByPet(petId)
        )
    }

    fun insertRequest(request: Request) {
        requestRepository.insertRequest(request)
        getRequestsByUser(request.ownerId)
    }

    fun updateRequest(request: Request) {
        requestRepository.updateRequest(request)
        getRequestsByUser(request.ownerId)
    }

    fun deleteRequest(request: Request) {
        requestRepository.deleteRequest(request)
        getRequestsByUser(request.ownerId)
    }
}

