package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pawdopt.data.model.RegisterFormState
import com.example.pawdopt.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel : ViewModel() {

    private val _formState = MutableStateFlow(RegisterFormState())
    val formState: StateFlow<RegisterFormState> = _formState.asStateFlow()

    fun onNombreChange(value: String) {
        _formState.value = _formState.value.copy(nombre = value)
        validate()
    }

    fun onEmailChange(value: String) {
        _formState.value = _formState.value.copy(email = value)
        validate()
    }

    fun onPasswordChange(value: String) {
        _formState.value = _formState.value.copy(password = value)
        validate()
    }

    fun onConfirmarPasswordChange(value: String) {
        _formState.value = _formState.value.copy(confirmarPassword = value)
        validate()
    }

    private fun validate() {
        val current = _formState.value
        val nombreError = if (current.nombre.isBlank()) "El nombre es obligatorio" else null
        val emailError = if (!current.email.contains("@")) "Email inválido" else null
        val passwordError = if (current.password.length < 6) "Mínimo 6 caracteres" else null
        val confirmarPasswordError =
            if (current.password != current.confirmarPassword) "Las contraseñas no coinciden" else null

        val isValid = listOf(
            nombreError, emailError, passwordError, confirmarPasswordError
        ).all { it == null }

        _formState.value = current.copy(
            nombreError = nombreError,
            emailError = emailError,
            passwordError = passwordError,
            confirmarPasswordError = confirmarPasswordError,
            isValid = isValid
        )
    }
    fun registerUser(userViewModel: UserViewModel, onSuccess: () -> Unit = {}) {
        val current = _formState.value
        if (!current.isValid) return

        val newUser = User(
            nombre = current.nombre,
            email = current.email,
            password = current.password
        )
        userViewModel.registerUser(newUser)

        _formState.value = RegisterFormState()

        onSuccess()
    }
}