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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.example.pawdopt.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    navController: NavHostController,
    viewModel: AddPetViewModel = viewModel(),
    petViewModel: PetViewModel,
    userViewModel: UserViewModel
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

        // --- Imagen seleccionada ---
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

        // --- Campos ---
        OutlinedTextField(
            value = state.nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.especie,
            onValueChange = viewModel::onEspecieChange,
            label = { Text("Especie") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.edad,
            onValueChange = viewModel::onEdadChange,
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.raza,
            onValueChange = viewModel::onRazaChange,
            label = { Text("Raza") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.descripcion,
            onValueChange = viewModel::onDescripcionChange,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.ubicacion,
            onValueChange = viewModel::onUbicacionChange,
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth()
        )

        // --- Botón Guardar ---
        Button(
            onClick = {
                val user = userViewModel.state.value.currentUser

                if (user == null) {
                    Toast.makeText(context, "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.LOGIN)
                    return@Button
                }

                viewModel.agregarMascota(
                    context = context,
                    userId = user.id,
                    onSuccess = {
                        Toast.makeText(context, "Mascota agregada", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    onError = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                )
            },
            enabled = viewModel.canSave(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Mascota")
        }
    }
}