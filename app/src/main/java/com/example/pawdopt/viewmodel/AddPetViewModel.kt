package com.example.pawdopt.viewmodel

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawdopt.data.model.Pet
import com.example.pawdopt.data.remote.repository.CloudinaryRepository
import com.example.pawdopt.data.remote.repository.PetRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import android.content.Context
import com.example.pawdopt.data.model.AddPetFormState


class AddPetViewModel(
    private val petRepo: PetRemoteRepository = PetRemoteRepository(),
    private val cloudRepo: CloudinaryRepository = CloudinaryRepository()
) : ViewModel() {

    private val _formState = MutableStateFlow(AddPetFormState())
    val formState: StateFlow<AddPetFormState> = _formState

    fun onNombreChange(v: String) { _formState.value = _formState.value.copy(nombre = v) }
    fun onEspecieChange(v: String) { _formState.value = _formState.value.copy(especie = v) }
    fun onEdadChange(v: String) { _formState.value = _formState.value.copy(edad = v) }
    fun onRazaChange(v: String) { _formState.value = _formState.value.copy(raza = v) }
    fun onDescripcionChange(v: String) { _formState.value = _formState.value.copy(descripcion = v) }
    fun onUbicacionChange(v: String) { _formState.value = _formState.value.copy(ubicacion = v) }

    fun onFotoSeleccionada(uri: String) {
        _formState.value = _formState.value.copy(fotoUri = uri)
    }

    fun agregarMascota(
        context: Context,
        userId: Long,
        onDone: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val uri = _formState.value.fotoUri ?: return@launch

            val bytes = convertUriToBytes(context, Uri.parse(uri))

            val imageUrl = cloudRepo.uploadImage(bytes)

            val pet = Pet(
                id = 0,
                nombre = _formState.value.nombre,
                especie = _formState.value.especie,
                edad = _formState.value.edad.toInt(),
                raza = _formState.value.raza,
                descripcion = _formState.value.descripcion,
                fotoUri = imageUrl,
                ubicacion = _formState.value.ubicacion,
                ownerId = userId
            )

            petRepo.addPet(pet)

            onDone()
        }
    }

    private fun convertUriToBytes(context: Context, uri: Uri): ByteArray {
        val source = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.createSource(context.contentResolver, uri)
        } else {
            return byteArrayOf()
        }

        val bitmap = ImageDecoder.decodeBitmap(source)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        return baos.toByteArray()
    }
}