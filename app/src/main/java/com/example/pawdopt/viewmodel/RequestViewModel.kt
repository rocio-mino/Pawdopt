package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawdopt.data.local.RequestEntity
import com.example.pawdopt.data.repository.RequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RequestState(
    val requests: List<RequestEntity> = emptyList(),
    val selectedRequest: RequestEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RequestState())
    val state: StateFlow<RequestState> = _state.asStateFlow()

    fun getRequestsByUser(userId: Int) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }
                val requests = requestRepository.getRequestsByUser(userId)
                _state.update { it.copy(isLoading = false, requests = requests) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun getRequestsByPet(petId: Int) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }
                val requests = requestRepository.getRequestsByPet(petId)
                _state.update { it.copy(isLoading = false, requests = requests) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun insertRequest(request: RequestEntity) {
        viewModelScope.launch {
            requestRepository.insertRequest(request)
        }
    }

    fun updateRequest(request: RequestEntity) {
        viewModelScope.launch {
            requestRepository.updateRequest(request)
        }
    }

    fun deleteRequest(request: RequestEntity) {
        viewModelScope.launch {
            requestRepository.deleteRequest(request)
        }
    }
}

