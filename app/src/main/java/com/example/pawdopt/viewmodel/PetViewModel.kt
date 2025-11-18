package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawdopt.data.model.Pet
import com.example.pawdopt.data.remote.repository.PetRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class PetFilter { TODOS, PERROS, GATOS }

data class PetState(
    val pets: List<Pet> = emptyList(),
    val filter: PetFilter = PetFilter.TODOS
)

class PetViewModel(
    private val repo: PetRemoteRepository = PetRemoteRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(PetState())
    val state: StateFlow<PetState> = _state

    fun getAllPets() {
        viewModelScope.launch {
            val pets = repo.getAllPets()
            applyFilter(_state.value.filter, pets)
        }
    }

    fun setFilter(filter: PetFilter) {
        viewModelScope.launch {
            applyFilter(filter, repo.getAllPets())
        }
    }

    private fun applyFilter(filter: PetFilter, list: List<Pet>) {
        val filtered = when (filter) {
            PetFilter.TODOS -> list
            PetFilter.PERROS -> list.filter { it.especie.equals("perro", true) }
            PetFilter.GATOS -> list.filter { it.especie.equals("gato", true) }
        }

        _state.value = _state.value.copy(
            filter = filter,
            pets = filtered
        )
    }
}