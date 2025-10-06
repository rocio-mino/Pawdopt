package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pawdopt.data.model.LoginFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState.asStateFlow()

    fun onEmailChange(value: String) {
        _formState.value = _formState.value.copy(email = value, loginError = null)
        validate()
    }

    fun onPasswordChange(value: String) {
        _formState.value = _formState.value.copy(password = value, loginError = null)
        validate()
    }

    private fun validate() {
        val current = _formState.value
        val emailError = if (!current.email.contains("@")) "Email inválido" else null
        val passwordError = if (current.password.isBlank()) "La contraseña es obligatoria" else null

        val isValid = listOf(emailError, passwordError).all { it == null }

        _formState.value = current.copy(
            emailError = emailError,
            passwordError = passwordError,
            isValid = isValid
        )
    }

    fun loginUser(userViewModel: UserViewModel, onSuccess: () -> Unit) {
        val state = _formState.value
        if (state.isValid) {
            val user = userViewModel.loginUser(state.email, state.password)
            if (user != null) {
                onSuccess()
            } else {
                _formState.value = _formState.value.copy(loginError = "Credenciales incorrectas")
            }
        }
    }
}
