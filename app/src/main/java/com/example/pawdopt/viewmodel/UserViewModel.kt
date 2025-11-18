package com.example.pawdopt.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawdopt.data.local.UserPreferencesDataStore
import com.example.pawdopt.data.model.User
import com.example.pawdopt.data.remote.repository.CloudinaryRepository
import com.example.pawdopt.data.remote.repository.UserRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

data class UserState(
    val currentUser: User? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class UserViewModel(
    private val prefs: UserPreferencesDataStore,
    private val userRepo: UserRemoteRepository = UserRemoteRepository(),
    private val cloudRepo: CloudinaryRepository = CloudinaryRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state

    init {
        restoreSession()
    }

    private fun restoreSession() {
        viewModelScope.launch {
            prefs.currentUserIdFlow.collect { id ->
                if (id != 0L) {
                    try {
                        val user = userRepo.getUser(id)
                        _state.value = UserState(currentUser = user)
                    } catch (e: Exception) {
                        _state.value = UserState(currentUser = null)
                    }
                }
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val user = userRepo.login(email, password)

                if (user == null) {
                    onError("Credenciales incorrectas")
                    return@launch
                }

                // Guardar sesión
                prefs.saveCurrentUserId(user.id)

                // Actualizar estado
                _state.value = UserState(currentUser = user)

                onSuccess()

            } catch (e: Exception) {
                onError("Error de servidor")
            }
        }
    }

    fun register(
        nombre: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val newUser = User(
                    id = 0,
                    nombre = nombre,
                    email = email,
                    password = password,
                    fotoUri = null
                )

                val saved = userRepo.register(newUser)

                prefs.saveCurrentUserId(saved.id)

                _state.value = UserState(currentUser = saved)

                onSuccess()

            } catch (e: Exception) {
                onError("No se pudo registrar")
            }
        }
    }

    fun getUserById(id: Long, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                onResult(userRepo.getUser(id))
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            prefs.clearCurrentUserId()
            _state.value = UserState(currentUser = null)
        }
    }

    fun updatePhoto(
        context: Context,
        uri: Uri,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val user = _state.value.currentUser ?: return onError()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bytes = convertUriToBytes(context, uri)

                val url = cloudRepo.uploadImage(bytes)

                userRepo.updatePhoto(user.id, url)

                val updated = user.copy(fotoUri = url)
                _state.value = UserState(currentUser = updated)

                prefs.saveCurrentUserId(updated.id)

                onSuccess()

            } catch (e: Exception) {
                e.printStackTrace()
                onError()
            }
        }
    }

    private fun convertUriToBytes(context: Context, uri: Uri): ByteArray {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }

        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)
        return output.toByteArray()
    }
}