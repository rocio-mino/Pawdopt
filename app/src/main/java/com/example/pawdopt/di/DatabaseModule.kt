package com.example.pawdopt.di

import android.content.Context
import androidx.room.Room
import com.example.pawdopt.data.local.AppDatabase
import com.example.pawdopt.data.local.PetDao
import com.example.pawdopt.data.local.RequestDao
import com.example.pawdopt.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "pawdopt_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun providePetDao(db: AppDatabase): PetDao = db.petDao()

    @Provides
    fun provideRequestDao(db: AppDatabase): RequestDao = db.requestDao()
}