package com.example.pawdopt.data.repository

import com.example.pawdopt.data.model.Pet

class PetRepository {

    private val pets = mutableListOf(
        Pet(
            id = 1,
            nombre = "Firulais",
            especie = "Perro",
            edad = 3,
            raza = "Labrador",
            descripcion = "Muy juguetón y amigable",
            fotoUri = null,
            ubicacion = "Santiago",
            userId = 1
        ),
        Pet(
            id = 2,
            nombre = "Mishi",
            especie = "Gato",
            edad = 2,
            raza = "Persa",
            descripcion = "Tranquilo y dormilón",
            fotoUri = null,
            ubicacion = "Valparaíso",
            userId = 2
        )
    )

    fun getAllPets(): List<Pet> = pets
    fun getPetById(id: Int): Pet? = pets.find { it.id == id }
    fun insertPet(pet: Pet) {
        pets.add(pet.copy(id = pets.size + 1))
    }
    fun updatePet(pet: Pet) {
        val index = pets.indexOfFirst { it.id == pet.id }
        if (index != -1) pets[index] = pet
    }
    fun deletePet(pet: Pet) {
        pets.removeIf { it.id == pet.id }
    }
}
