package com.example.pawdopt.data.remote.repository

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class CloudinaryRepository {

    private val cloudName = "dlsygqy0s"
    private val uploadPreset = "pawdopt_unsigned"

    fun uploadImage(imageBytes: ByteArray): String {
        val url = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "image.jpg",
                imageBytes.toRequestBody("image/jpeg".toMediaType()))
            .addFormDataPart("upload_preset", uploadPreset)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("Error al subir imagen: ${response.code}")
        }

        val bodyString = response.body?.string() ?: throw Exception("Respuesta vacía")
        val json = JSONObject(bodyString)

        return json.getString("secure_url")
    }
}
