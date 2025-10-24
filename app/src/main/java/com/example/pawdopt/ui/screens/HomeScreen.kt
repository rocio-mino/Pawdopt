package com.example.pawdopt.ui.screens

import PawSurface2
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pawdopt.R
import com.example.pawdopt.component.PetCard
import com.example.pawdopt.navigation.Routes
import com.example.pawdopt.viewmodel.PetFilter
import com.example.pawdopt.viewmodel.PetViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: PetViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val colors = MaterialTheme.colorScheme

    // Recarga los datos cada vez que el usuario vuelve al Home
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.getAllPets()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.ADD_PET) },
                containerColor = colors.primary
            ) {
                Icon(Icons.Default.AddCircle, contentDescription = "Agregar mascota")
            }
        },
        containerColor = colors.background
    ) { innerPadding ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            // Fondo con patrón de patas
            Image(
                painter = painterResource(id = R.drawable.fondo_patas),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Título con fondo claro
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = PawSurface2)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "🐾 Encuentra tu nuevo amigo",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Chips de filtro estilizados y centrados
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        selected = state.filter == PetFilter.TODOS,
                        onClick = { viewModel.setFilter(PetFilter.TODOS) },
                        label = { Text("Todos") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colors.primary,
                            selectedLabelColor = colors.onPrimary,
                            containerColor = colors.surface,
                            labelColor = colors.onSurface
                        )
                    )
                    FilterChip(
                        selected = state.filter == PetFilter.PERROS,
                        onClick = { viewModel.setFilter(PetFilter.PERROS) },
                        label = { Text("Perros\uD83D\uDC36") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colors.primary,
                            selectedLabelColor = colors.onPrimary,
                            containerColor = colors.surface,
                            labelColor = colors.onSurface
                        )
                    )
                    FilterChip(
                        selected = state.filter == PetFilter.GATOS,
                        onClick = { viewModel.setFilter(PetFilter.GATOS) },
                        label = { Text("Gatos\uD83D\uDC31") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colors.primary,
                            selectedLabelColor = colors.onPrimary,
                            containerColor = colors.surface,
                            labelColor = colors.onSurface
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de mascotas
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.pets) { pet ->
                        PetCard(
                            pet = pet,
                            onClick = { navController.navigate(Routes.petDetailRoute(pet.id)) }
                        )
                    }
                }
            }
        }
    }
}