package com.example.finalproject.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finalproject.data.Pet

@Composable
fun PetScreen(
    modifier: Modifier = Modifier,
    pets: List<Pet>,
    onNavigateToNewPet: () -> Unit,
    onEdit: (Pet) -> Unit,
) {

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(pets) { pet ->
                PetItem(
                    pet = pet,
                    onEdit = {onEdit(pet)}
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        FloatingActionButton(
            onClick = onNavigateToNewPet,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Add New Pet", Modifier.padding(8.dp))
                Icon(Icons.Filled.Add, contentDescription = "Add Pet", Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun PetItem(
    pet: Pet,
    onEdit: (Pet) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {isExpanded = !isExpanded}
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Sharp.AccountCircle,
                contentDescription = "Pet Picture",
                modifier = Modifier.size(60.dp)
            )
            Text(text = pet.name)
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Animal: ${pet.animal}")
                Text("Age: ${pet.age}")
                Text("Colour: ${pet.colour}")
                Text("Breed: ${pet.breed}")
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        onEdit(pet)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Pet")
                }
            }
        }
    }
}