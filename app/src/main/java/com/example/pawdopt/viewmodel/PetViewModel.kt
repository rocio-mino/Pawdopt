package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pawdopt.data.model.Pet
import com.example.pawdopt.data.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class PetState(
    val pets: List<Pet> = emptyList(),
    val selectedPet: Pet? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PetViewModel @Inject constructor(
    private val petRepository: PetRepository = PetRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(PetState())
    val state: StateFlow<PetState> = _state.asStateFlow()

    init {
        getAllPets()
    }
    fun getAllPets() {
        _state.value = _state.value.copy(pets = petRepository.getAllPets())
    }
    fun getPetById(petId: Int) {
        _state.value = _state.value.copy(selectedPet = petRepository.getPetById(petId))
    }
    fun insertPet(pet: Pet) {
        petRepository.insertPet(pet)
        getAllPets()
    }
    fun updatePet(pet: Pet) {
        petRepository.updatePet(pet)
        getAllPets()
    }
    fun deletePet(pet: Pet) {
        petRepository.deletePet(pet)
        getAllPets()
    }
}
