package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pawdopt.data.model.AddPetFormState
import com.example.pawdopt.data.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddPetViewModel : ViewModel() {

    private val _formState = MutableStateFlow(AddPetFormState())
    val formState: StateFlow<AddPetFormState> = _formState.asStateFlow()

    fun onNombreChange(value: String) {
        _formState.value = _formState.value.copy(nombre = value)
        validate()
    }

    fun onEspecieChange(value: String) {
        _formState.value = _formState.value.copy(especie = value)
        validate()
    }

    fun onEdadChange(value: String) {
        _formState.value = _formState.value.copy(edad = value)
        validate()
    }

    fun onRazaChange(value: String) {
        _formState.value = _formState.value.copy(raza = value)
        validate()
    }

    fun onDescripcionChange(value: String) {
        _formState.value = _formState.value.copy(descripcion = value)
        validate()
    }

    fun onUbicacionChange(value: String) {
        _formState.value = _formState.value.copy(ubicacion = value)
        validate()
    }

    fun onFotoSeleccionada(uri: String?) {
        _formState.value = _formState.value.copy(fotoUri = uri)
        validate()
    }

    private fun validate() {
        val current = _formState.value

        val nombreError = if (current.nombre.isBlank()) "El nombre es obligatorio" else null
        val especieError = if (current.especie.isBlank()) "La especie es obligatoria" else null
        val edadError = if (current.edad.isBlank() || current.edad.toIntOrNull() == null)
            "Edad inválida" else null
        val razaError = if (current.raza.isBlank()) "La raza es obligatoria" else null
        val descripcionError = if (current.descripcion.isBlank()) "La descripción es obligatoria" else null
        val ubicacionError = if (current.ubicacion.isBlank()) "La ubicación es obligatoria" else null

        val isValid = listOf(
            nombreError, especieError, edadError, razaError, descripcionError, ubicacionError
        ).all { it == null }

        _formState.value = current.copy(
            nombreError = nombreError,
            especieError = especieError,
            edadError = edadError,
            razaError = razaError,
            descripcionError = descripcionError,
            ubicacionError = ubicacionError,
            isValid = isValid
        )
    }

    fun agregarMascota(petViewModel: PetViewModel, ownerId: Int, onSuccess: () -> Unit) {
        val state = _formState.value
        if (state.isValid) {
            val nuevaMascota = Pet(
                nombre = state.nombre,
                especie = state.especie,
                edad = state.edad.toInt(),
                raza = state.raza,
                descripcion = state.descripcion,
                ubicacion = state.ubicacion,
                fotoUri = state.fotoUri,
                ownerId = ownerId
            )
            petViewModel.insertPet(nuevaMascota)
            onSuccess()
        }
    }
}
