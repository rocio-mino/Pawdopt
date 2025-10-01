package com.example.pawdopt.data.repository

import com.example.pawdopt.data.model.User

class UserRepository {

    private val users = mutableListOf<User>()
    fun insertUser(user: User) {
        users.add(user.copy(id = users.size + 1))
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
}
