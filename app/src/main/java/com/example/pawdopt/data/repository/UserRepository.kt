package com.example.pawdopt.data.repository

import com.example.pawdopt.data.model.User

class UserRepository {
    private val users = mutableListOf(
        User(
            id = 1,
            nombre = "Ana Torres",
            email = "ana@example.com",
            password = "123456",
            fotoUri = null
        ),
        User(
            id = 2,
            nombre = "Carlos Pérez",
            email = "carlos@example.com",
            password = "abcdef",
            fotoUri = null
        )
    )

    private var nextId = users.maxOfOrNull { it.id }?.plus(1) ?: 1

    fun insertUser(user: User): User {
        val newUser = user.copy(id = nextId++)
        users.add(newUser)
        return newUser
    }

    fun updateUser(user: User) {
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) users[index] = user
    }

    fun deleteUser(user: User) {
        users.removeIf { it.id == user.id }
    }

    fun getUserById(userId: Int): User? = users.find { it.id == userId }

    fun getAllUsers(): List<User> = users

    fun getUserByEmailAndPassword(email: String, password: String): User? {
        return users.find {
            it.email.trim().equals(email.trim(), ignoreCase = true) &&
                    it.password.trim() == password.trim()
        }
    }
}
