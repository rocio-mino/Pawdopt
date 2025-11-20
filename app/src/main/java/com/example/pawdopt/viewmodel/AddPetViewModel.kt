package com.example.pawdopt.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawdopt.data.model.AddPetFormState
import com.example.pawdopt.data.model.Pet
import com.example.pawdopt.data.remote.repository.CloudinaryRepository
import com.example.pawdopt.data.remote.repository.PetRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddPetViewModel(
    private val petRepo: PetRemoteRepository = PetRemoteRepository(),
    private val cloudRepo: CloudinaryRepository = CloudinaryRepository()
) : ViewModel() {

    private val _formState = MutableStateFlow(AddPetFormState())
    val formState: StateFlow<AddPetFormState> = _formState


    fun onNombreChange(v: String) = update { it.copy(nombre = v) }
    fun onEspecieChange(v: String) = update { it.copy(especie = v) }
    fun onEdadChange(v: String) = update { it.copy(edad = v) }
    fun onRazaChange(v: String) = update { it.copy(raza = v) }
    fun onDescripcionChange(v: String) = update { it.copy(descripcion = v) }
    fun onUbicacionChange(v: String) = update { it.copy(ubicacion = v) }

    fun onFotoSeleccionada(uri: String) = update { it.copy(fotoUri = uri) }

    private fun update(block: (AddPetFormState) -> AddPetFormState) {
        _formState.value = block(_formState.value)
    }

    fun canSave(): Boolean {
        val s = _formState.value

        return s.nombre.isNotBlank() &&
                s.especie.isNotBlank() &&
                s.edad.isNotBlank() &&
                s.edad.toIntOrNull() != null &&
                s.raza.isNotBlank() &&
                s.descripcion.isNotBlank() &&
                s.ubicacion.isNotBlank() &&
                s.fotoUri != null
    }

    fun agregarMascota(
        context: Context,
        userId: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!canSave()) {
            onError("Completa todos los campos")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val s = _formState.value
                val edadInt = s.edad.toIntOrNull() ?: throw Exception("Edad inválida")

                val bytes = convertUriToBytes(context, Uri.parse(s.fotoUri!!))

                val uploadedUrl = cloudRepo.uploadImage(bytes)
                    ?: throw Exception("Error subiendo imagen")

                val pet = Pet(
                    id = 0,
                    nombre = s.nombre,
                    especie = s.especie,
                    edad = edadInt,
                    raza = s.raza,
                    descripcion = s.descripcion,
                    fotoUri = uploadedUrl,
                    ubicacion = s.ubicacion,
                    ownerId = userId
                )

                petRepo.addPet(pet)

                onSuccess()

            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }

    private fun convertUriToBytes(context: Context, uri: Uri): ByteArray {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            android.provider.MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )
        }

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        return baos.toByteArray()
    }
}