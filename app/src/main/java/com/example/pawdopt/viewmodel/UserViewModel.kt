package com.example.pawdopt.viewmodel

import com.example.pawdopt.data.model.User
import com.example.pawdopt.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawdopt.data.local.UserPreferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class UserState(
    val currentUser: User? = null,
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository()
    private val prefs = UserPreferencesDataStore(application.applicationContext)

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            prefs.currentUserIdFlow.collect { storedId ->
                if (storedId != 0) {
                    val user = userRepository.getUserById(storedId)
                    _state.value = _state.value.copy(currentUser = user)
                }
            }
        }
    }

    fun registerUser(user: User) {
        val newUser = userRepository.insertUser(user)
        _state.value = _state.value.copy(currentUser = newUser)
        viewModelScope.launch { prefs.saveCurrentUserId(newUser.id) }
    }

    fun loginUser(email: String, password: String): User? {
        val user = userRepository.getUserByEmailAndPassword(email, password)
        _state.value = _state.value.copy(currentUser = user)
        viewModelScope.launch {
            if (user != null) prefs.saveCurrentUserId(user.id)
        }
        return user
    }

    fun logout() {
        _state.value = _state.value.copy(currentUser = null)
        viewModelScope.launch { prefs.clearCurrentUserId() }
    }

    fun updateUser(user: User) {
        userRepository.updateUser(user)
        _state.value = _state.value.copy(currentUser = user)
        viewModelScope.launch {
            if (user.id != 0) prefs.saveCurrentUserId(user.id)
        }
    }

    fun getUserById(id: Int) {
        _state.value = _state.value.copy(currentUser = userRepository.getUserById(id))
    }

    fun findUserById(id: Int): User? {
        return userRepository.getUserById(id)
    }

    fun getAllUsers() {
        _state.value = _state.value.copy(users = userRepository.getAllUsers())
    }
}




