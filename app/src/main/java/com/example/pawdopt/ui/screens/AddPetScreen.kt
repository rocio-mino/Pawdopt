package com.example.pawdopt.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.pawdopt.navigation.Routes
import com.example.pawdopt.viewmodel.AddPetViewModel
import com.example.pawdopt.viewmodel.PetViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    navController: NavHostController,
    viewModel: AddPetViewModel = viewModel(),
    petViewModel: PetViewModel,
    currentUserId: Int
) {
    val state by viewModel.formState.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onFotoSeleccionada(it.toString()) }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (state.fotoUri != null) {
            Image(
                painter = rememberAsyncImagePainter(state.fotoUri),
                contentDescription = "Foto de la mascota",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Sin imagen seleccionada")
            }
        }

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar imagen")
        }

        OutlinedTextField(
            value = state.nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text("Nombre") },
            isError = state.nombreError != null,
            modifier = Modifier.fillMaxWidth()
        )
        state.nombreError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        OutlinedTextField(
            value = state.especie,
            onValueChange = viewModel::onEspecieChange,
            label = { Text("Especie (Perro, Gato, etc.)") },
            isError = state.especieError != null,
            modifier = Modifier.fillMaxWidth()
        )
        state.especieError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        OutlinedTextField(
            value = state.edad,
            onValueChange = viewModel::onEdadChange,
            label = { Text("Edad") },
            isError = state.edadError != null,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        state.edadError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        OutlinedTextField(
            value = state.raza,
            onValueChange = viewModel::onRazaChange,
            label = { Text("Raza") },
            isError = state.razaError != null,
            modifier = Modifier.fillMaxWidth()
        )
        state.razaError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        OutlinedTextField(
            value = state.descripcion,
            onValueChange = viewModel::onDescripcionChange,
            label = { Text("Descripción") },
            isError = state.descripcionError != null,
            modifier = Modifier.fillMaxWidth()
        )
        state.descripcionError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        OutlinedTextField(
            value = state.ubicacion,
            onValueChange = viewModel::onUbicacionChange,
            label = { Text("Ubicación") },
            isError = state.ubicacionError != null,
            modifier = Modifier.fillMaxWidth()
        )
        state.ubicacionError?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

        Button(
            onClick = {
                viewModel.agregarMascota(petViewModel, currentUserId) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                    Toast.makeText(context, "Mascota agregada", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = state.isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Mascota")
        }
    }
}



