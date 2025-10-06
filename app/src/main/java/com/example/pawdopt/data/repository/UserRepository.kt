package com.example.pawdopt.data.repository

import com.example.pawdopt.data.model.User

class UserRepository {
    private val users = mutableListOf<User>()
    private var nextId = 1

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
