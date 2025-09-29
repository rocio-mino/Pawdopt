package com.example.pawdopt.data.local

import androidx.room.*

@Dao
interface RequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: RequestEntity)

    @Update
    suspend fun updateRequest(request: RequestEntity)

    @Delete
    suspend fun deleteRequest(request: RequestEntity)

    @Query("SELECT * FROM requests WHERE userId = :userId")
    suspend fun getRequestsByUser(userId: Int): List<RequestEntity>

    @Query("SELECT * FROM requests WHERE petId = :petId")
    suspend fun getRequestsByPet(petId: Int): List<RequestEntity>


}