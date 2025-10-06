package com.example.pawdopt.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pawdopt.data.model.Pet

@Composable
fun PetCard(pet: Pet, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = pet.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${pet.especie} • ${pet.raza} • ${pet.edad} años",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = pet.descripcion,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
    }
}