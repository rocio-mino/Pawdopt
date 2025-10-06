package com.example.pawdopt.data.model

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isValid: Boolean = false,
    val loginError: String? = null
)
