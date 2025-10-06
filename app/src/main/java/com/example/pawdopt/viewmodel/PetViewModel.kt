package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pawdopt.data.model.Pet
import com.example.pawdopt.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class PetFilter { TODOS, PERROS, GATOS }

data class PetState(
    val pets: List<Pet> = emptyList(),
    val selectedPet: Pet? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val filter: PetFilter = PetFilter.TODOS
)

class PetViewModel(
    private val petRepository: PetRepository = PetRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(PetState())
    val state: StateFlow<PetState> = _state.asStateFlow()

    init {
        getAllPets()
    }

    fun getAllPets() {
        try {
            _state.value = _state.value.copy(isLoading = true)
            val pets = petRepository.getAllPets()
            _state.value = _state.value.copy(
                pets = applyFilter(pets, _state.value.filter),
                isLoading = false
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(error = e.message, isLoading = false)
        }
    }

    fun getPetById(petId: Int) {
        try {
            _state.value = _state.value.copy(isLoading = true)
            val pet = petRepository.getPetById(petId)
            _state.value = _state.value.copy(
                selectedPet = pet,
                isLoading = false
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                error = e.message,
                isLoading = false
            )
        }
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

    fun setFilter(filter: PetFilter) {
        val allPets = petRepository.getAllPets()
        _state.value = _state.value.copy(
            filter = filter,
            pets = applyFilter(allPets, filter)
        )
    }

    private fun applyFilter(pets: List<Pet>, filter: PetFilter): List<Pet> {
        return when (filter) {
            PetFilter.TODOS -> pets
            PetFilter.PERROS -> pets.filter { it.especie.equals("Perro", ignoreCase = true) }
            PetFilter.GATOS -> pets.filter { it.especie.equals("Gato", ignoreCase = true) }
        }
    }
}



