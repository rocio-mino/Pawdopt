package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawdopt.data.local.UserEntity
import com.example.pawdopt.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserState(
    val currentUser: UserEntity? = null,
    val users: List<UserEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    fun registerUser(user: UserEntity) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true, error = null) }
                userRepository.insertUser(user)
                _state.update { it.copy(isLoading = false, currentUser = user) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun getUserById(id: Int) {
        viewModelScope.launch {
            userRepository.getUserById(id).collect { user ->
                _state.update { it.copy(currentUser = user) }
            }
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            userRepository.getAllUsers().collect { users ->
                _state.update { it.copy(users = users) }
            }
        }
    }
}
