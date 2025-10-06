package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pawdopt.data.model.User
import com.example.pawdopt.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserState(
    val currentUser: User? = null,
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class UserViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    fun registerUser(user: User) {
        val newUser = userRepository.insertUser(user)
        _state.value = _state.value.copy(currentUser = newUser)
    }

    fun loginUser(email: String, password: String): User? {
        val user = userRepository.getUserByEmailAndPassword(email, password)
        _state.value = _state.value.copy(currentUser = user)
        return user
    }

    fun logout() {
        _state.value = _state.value.copy(currentUser = null)
    }

    fun updateUser(user: User) {
        userRepository.updateUser(user)
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


