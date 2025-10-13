package com.example.pawdopt.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.pawdopt.data.model.AdoptionRequest
import com.example.pawdopt.viewmodel.AdoptionViewModel
import com.example.pawdopt.viewmodel.UserViewModel

@Composable
fun MyRequestsScreen(
    navController: NavHostController,
    adoptionViewModel: AdoptionViewModel,
    userViewModel: UserViewModel
) {
    val state by adoptionViewModel.state.collectAsState()
    val currentUser = userViewModel.state.collectAsState().value.currentUser
    val context = LocalContext.current

    if (currentUser == null) {
        Text("Inicia sesión para ver tus solicitudes.")
        return
    }

    LaunchedEffect(currentUser.id) {
        adoptionViewModel.refreshAll()
    }

    val sent = state.requests.filter { it.adopterId == currentUser.id }
    val received = state.requests.filter { it.ownerId == currentUser.id }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Enviadas", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        if (sent.isEmpty()) Text("No has enviado solicitudes.")
        else {
            LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                items(sent) { req ->
                    RequestRow(
                        req = req,
                        isOwner = false,
                        userViewModel = userViewModel,
                        onAccept = {},
                        onReject = {}
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Recibidas", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        if (received.isEmpty()) Text("No tienes solicitudes recibidas.")
        else {
            LazyColumn {
                items(received) { req ->
                    RequestRow(
                        req = req,
                        isOwner = true,
                        userViewModel = userViewModel,
                        onAccept = {
                            adoptionViewModel.acceptRequest(req.id)
                            adoptionViewModel.refreshAll()
                            Toast.makeText(context, "Solicitud aceptada", Toast.LENGTH_SHORT).show()
                        },
                        onReject = {
                            adoptionViewModel.rejectRequest(req.id)
                            adoptionViewModel.refreshAll()
                            Toast.makeText(context, "Solicitud rechazada y eliminada", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RequestRow(
    req: AdoptionRequest,
    isOwner: Boolean,
    userViewModel: UserViewModel,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    // Buscar datos de dueño y adoptante
    val adopter = userViewModel.findUserById(req.adopterId)
    val owner = userViewModel.findUserById(req.ownerId)

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Mascota ID: ${req.petId}")
            Text("Estado: ${req.status}")
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Dueño: ${owner?.nombre ?: "Desconocido"} (${owner?.email ?: "?"})",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Solicitante: ${adopter?.nombre ?: "Desconocido"} (${adopter?.email ?: "?"})",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))
            if (isOwner) {
                Row {
                    Button(onClick = onAccept, modifier = Modifier.weight(1f)) {
                        Text("Aceptar")
                    }
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(onClick = onReject, modifier = Modifier.weight(1f)) {
                        Text("Rechazar")
                    }
                }
            }
        }
    }
}
