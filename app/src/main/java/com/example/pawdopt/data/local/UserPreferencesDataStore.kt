package com.example.pawdopt.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPreferencesKeys {
    val CURRENT_USER_ID = longPreferencesKey("current_user_id")
}

class UserPreferencesDataStore(private val context: Context) {

    val currentUserIdFlow: Flow<Long> = context.dataStore.data
        .map { prefs ->
            prefs[UserPreferencesKeys.CURRENT_USER_ID] ?: 0L
        }

    suspend fun saveCurrentUserId(id: Long) {
        context.dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.CURRENT_USER_ID] = id
        }
    }

    suspend fun clearCurrentUserId() {
        context.dataStore.edit { prefs ->
            prefs.remove(UserPreferencesKeys.CURRENT_USER_ID)
        }
    }
}
