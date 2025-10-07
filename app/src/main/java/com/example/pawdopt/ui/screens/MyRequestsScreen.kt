package com.example.pawdopt.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.pawdopt.data.model.AdoptionRequest
import com.example.pawdopt.viewmodel.AdoptionViewModel
import com.example.pawdopt.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRequestsScreen(
    navController: NavHostController,
    adoptionViewModel: AdoptionViewModel,
    userViewModel: UserViewModel
) {
    val state by adoptionViewModel.state.collectAsState()
    val currentUser = userViewModel.state.collectAsState().value.currentUser

    Scaffold(topBar = { TopAppBar(title = { Text("Mis solicitudes") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (currentUser == null) {
                Text("Inicia sesión para ver tus solicitudes.")
                return@Column
            }

            LaunchedEffect(currentUser.id) {
                adoptionViewModel.refreshAll()
            }

            val sent = state.requests.filter { it.adopterId == currentUser.id }
            val received = state.requests.filter { it.ownerId == currentUser.id }

            Text("Enviadas", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            if (sent.isEmpty()) Text("No has enviado solicitudes.")
            else {
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(sent) { req ->
                        RequestRow(req = req, isOwner = false, onAccept = {}, onReject = {})
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
                            onAccept = { adoptionViewModel.acceptRequest(req.id) },
                            onReject = { adoptionViewModel.rejectRequest(req.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RequestRow(
    req: AdoptionRequest,
    isOwner: Boolean,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Mascota ID: ${req.petId}")
            Text("Solicitante ID: ${req.adopterId}")
            Text("Estado: ${req.status}")
            Spacer(Modifier.height(8.dp))
            if (isOwner) {
                Row {
                    Button(onClick = onAccept, modifier = Modifier.weight(1f)) { Text("Aceptar") }
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(onClick = onReject, modifier = Modifier.weight(1f)) { Text("Rechazar") }
                }
            }
        }
    }
}
