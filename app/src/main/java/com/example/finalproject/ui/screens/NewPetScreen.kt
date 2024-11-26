package com.example.finalproject.ui.screens

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finalproject.R
import com.example.finalproject.data.Pet
import com.example.finalproject.data.PetAppContentProvider
import com.example.finalproject.data.UserProfile
import com.example.finalproject.getUserPets

@Composable
fun NewPetScreen(
    modifier: Modifier = Modifier,
    addPet: (Pet, Context) -> Unit,
    onNavigateBack: () -> Unit,
    currentUserProfile: UserProfile?,
    updatePetList: (Pet) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var animal by remember { mutableStateOf("") }
    var colour by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.dog_logo),
            contentDescription = "A Dog Logo",
            modifier = Modifier
                .size(192.dp)
                .padding(8.dp)
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = animal,
            onValueChange = { animal = it },
            label = { Text("Type") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = colour,
            onValueChange = { colour = it },
            label = { Text("Colour") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = breed,
            onValueChange = { breed = it },
            label = { Text("Breed") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && age.isNotBlank() && animal.isNotBlank() && colour.isNotBlank() && breed.isNotBlank()) {
                    currentUserProfile?.let { userProfile ->
                        val newPet = Pet(
                            id = 0,
                            name = name,
                            age = age.toIntOrNull() ?: 0,
                            animal = animal,
                            colour = colour,
                            breed = breed,
                            owner = userProfile.username
                        )
                        addPet(newPet, context)
                        updatePetList(newPet)
                    }
                    onNavigateBack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Pet")
        }
    }
}

fun addPet(pet: Pet, context: Context) {
    val values = ContentValues().apply {
        put("name", pet.name)
        put("age", pet.age)
        put("animal", pet.animal)
        put("colour", pet.colour)
        put("breed", pet.breed)
        put("owner", pet.owner)
    }

    try {
        val uri = context.contentResolver.insert(PetAppContentProvider.PET_CONTENT_URI, values)

        if (uri != null) {
            Toast.makeText(context, "Pet added successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to add pet.", Toast.LENGTH_SHORT).show()
        }
    } catch (e: SQLException) {
        Toast.makeText(context, "Error while adding pet.", Toast.LENGTH_LONG).show()
    }
}