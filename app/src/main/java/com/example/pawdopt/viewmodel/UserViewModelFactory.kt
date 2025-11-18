package com.example.pawdopt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pawdopt.data.local.UserPreferencesDataStore

class UserViewModelFactory(
    private val prefs: UserPreferencesDataStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}