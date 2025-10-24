package com.example.pawdopt.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.navigation.NavHostController
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.pawdopt.viewmodel.UserViewModel
import com.example.pawdopt.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    val state by userViewModel.state.collectAsState()
    val user = state.currentUser
    val context = LocalContext.current

    // Si el usuario no ha iniciado sesión
    if (user == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No has iniciado sesión", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            ) {
                Text("Iniciar sesión")
            }
        }
        return
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val updatedUser = user.copy(fotoUri = it.toString())
            userViewModel.updateUser(updatedUser)
            Toast.makeText(context, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TopAppBar(title = { Text("Mi perfil") })

        Image(
            painter = rememberAsyncImagePainter(
                model = user.fotoUri
                    ?: "https://cdn-icons-png.flaticon.com/512/847/847969.png"
            ),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )

        TextButton(onClick = { launcher.launch("image/*") }) {
            Text("Cambiar foto de perfil")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = user.nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = user.email, fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                userViewModel.logout()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.HOME) { inclusive = false }
                    launchSingleTop = true
                }
                Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Cerrar sesión", color = Color.White)
        }
    }
}