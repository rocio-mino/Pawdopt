package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawdopt.data.local.PetEntity
import com.example.pawdopt.data.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PetState(
    val pets: List<PetEntity> = emptyList(),
    val selectedPet: PetEntity? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PetViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PetState())
    val state: StateFlow<PetState> = _state.asStateFlow()

    init {
        getAllPets()
    }

    private fun getAllPets() {
        viewModelScope.launch {
            petRepository.getAllPets().collect { pets ->
                _state.update { it.copy(pets = pets) }
            }
        }
    }

    fun getPetById(petId: Int) {
        viewModelScope.launch {
            petRepository.getPetById(petId).collect { pet ->
                _state.update { it.copy(selectedPet = pet) }
            }
        }
    }

    fun insertPet(pet: PetEntity) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                petRepository.insertPet(pet)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun updatePet(pet: PetEntity) {
        viewModelScope.launch {
            petRepository.updatePet(pet)
        }
    }

    fun deletePet(pet: PetEntity) {
        viewModelScope.launch {
            petRepository.deletePet(pet)
        }
    }
}
