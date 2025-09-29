package com.example.pawdopt.di

import com.example.pawdopt.data.local.PetDao
import com.example.pawdopt.data.local.RequestDao
import com.example.pawdopt.data.local.UserDao
import com.example.pawdopt.data.repository.PetRepository
import com.example.pawdopt.data.repository.RequestRepository
import com.example.pawdopt.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(userDao: UserDao): UserRepository = UserRepository(userDao)

    @Provides
    @Singleton
    fun providePetRepository(petDao: PetDao): PetRepository = PetRepository(petDao)

    @Provides
    @Singleton
    fun provideRequestRepository(requestDao: RequestDao): RequestRepository = RequestRepository(requestDao)
}