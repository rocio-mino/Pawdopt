package com.example.pawdopt.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.pawdopt.viewmodel.AdoptionViewModel
import com.example.pawdopt.viewmodel.PetViewModel
import com.example.pawdopt.viewmodel.UserViewModel
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import com.example.pawdopt.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    navController: NavHostController,
    petId: Long,
    petViewModel: PetViewModel,
    userViewModel: UserViewModel,
    adoptionViewModel: AdoptionViewModel
) {
    val state by petViewModel.state.collectAsState()
    val context = LocalContext.current

    val pet = state.pets.find { it.id == petId }

    if (pet == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Mascota no encontrada.")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de ${pet.nombre}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = rememberAsyncImagePainter(pet.fotoUri),
                contentDescription = pet.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))

            Text(pet.nombre, fontSize = 26.sp, fontWeight = FontWeight.Bold)

            Text("Especie: ${pet.especie}")
            Text("Raza: ${pet.raza}")
            Text("Edad: ${pet.edad} años")
            Text("Ubicación: ${pet.ubicacion}")
            Text(pet.descripcion)

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    val user = userViewModel.state.value.currentUser
                    if (user == null) {
                        navController.navigate(Routes.LOGIN)
                        return@Button
                    }

                    adoptionViewModel.createRequest(
                        adopterId = user.id,
                        ownerId = pet.ownerId,
                        petId = pet.id
                    )

                    Toast.makeText(
                        context,
                        "Solicitud enviada",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate(Routes.MY_REQUESTS)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Solicitar adopción")
            }
        }
    }
}