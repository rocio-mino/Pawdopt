package com.example.pawdopt.ui.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pawdopt.navigation.Routes
import com.example.pawdopt.viewmodel.PetViewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.pawdopt.viewmodel.AdoptionViewModel
import com.example.pawdopt.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    navController: NavHostController,
    petId: Int,
    petViewModel: PetViewModel,
    userViewModel: UserViewModel,
    adoptionViewModel: AdoptionViewModel
) {
    val petState by petViewModel.state.collectAsState()
    val userState by userViewModel.state.collectAsState()
    val context = LocalContext.current

    val pet = petState.pets.find { it.id == petId }

    if (pet == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Mascota no encontrada o ya fue adoptada.")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle Mascota ${pet.nombre}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
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

            Text(
                text = pet.nombre,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Especie: ${pet.especie}",
                fontSize = 18.sp
            )
            Text(
                text = "Raza: ${pet.raza}",
                fontSize = 18.sp
            )
            Text(
                text = "Edad: ${pet.edad} años",
                fontSize = 18.sp
            )
            Text(
                text = "Ubicación: ${pet.ubicacion ?: "No especificada"}",
                fontSize = 16.sp
            )
            Text(
                text = pet.descripcion,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    val currentUser = userViewModel.state.value.currentUser
                    if (currentUser == null) {

                        navController.navigate(Routes.LOGIN)
                    } else {
                        adoptionViewModel.createRequest(
                            adopterId = currentUser.id,
                            ownerId = pet.ownerId,
                            petId = pet.id
                        )

                        petViewModel.deletePet(pet)

                        Toast.makeText(
                            context,
                            "Solicitud enviada para adoptar a ${pet.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // - navigate() con popUpTo mantiene la BottomBar funcional
                        navController.popBackStack()
                        navController.navigate(Routes.MY_REQUESTS) {
                            popUpTo(Routes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Solicitar adopción", fontSize = 18.sp)
            }
        }
    }
}

