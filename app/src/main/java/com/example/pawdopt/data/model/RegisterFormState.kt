package com.example.pawdopt.data.model

data class RegisterFormState(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val nombreError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmarPasswordError: String? = null,
    val isValid: Boolean = false
)