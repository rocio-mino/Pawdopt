package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pawdopt.data.model.User
import com.example.pawdopt.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class UserState(
    val currentUser: User? = null,
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    fun registerUser(user: User) {
        userRepository.insertUser(user)
        _state.value = _state.value.copy(currentUser = user)
    }
    fun getUserById(id: Int) {
        _state.value = _state.value.copy(
            currentUser = userRepository.getUserById(id)
        )
    }
    fun getAllUsers() {
        _state.value = _state.value.copy(
            users = userRepository.getAllUsers()
        )
    }
}
