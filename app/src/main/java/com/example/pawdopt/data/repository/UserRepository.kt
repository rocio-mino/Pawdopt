package com.example.pawdopt.data.repository


import com.example.pawdopt.data.local.PetEntity
import com.example.pawdopt.data.local.UserDao
import com.example.pawdopt.data.local.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
){
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    suspend fun deleteUser(user: UserEntity) = userDao.deleteUser(user)

    fun getUserById(userId: Int): Flow<UserEntity?> = userDao.getUserById(userId)

    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
}