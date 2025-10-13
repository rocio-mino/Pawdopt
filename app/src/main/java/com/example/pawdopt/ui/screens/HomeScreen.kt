package com.example.pawdopt.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = state.filter == PetFilter.TODOS,
                onClick = { viewModel.setFilter(PetFilter.TODOS) },
                label = { Text("Todos") }
            )
            FilterChip(
                selected = state.filter == PetFilter.PERROS,
                onClick = { viewModel.setFilter(PetFilter.PERROS) },
                label = { Text("Perros") }
            )
            FilterChip(
                selected = state.filter == PetFilter.GATOS,
                onClick = { viewModel.setFilter(PetFilter.GATOS) },
                label = { Text("Gatos") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

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

