package com.example.finalproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.finalproject.data.Pet
import com.example.finalproject.data.UserProfile
import com.example.finalproject.getUserPets

@Composable
fun OtherProfileScreen(
    profiles: List<UserProfile>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(profiles) { profile ->
                val pets = getUserPets(context = LocalContext.current, profile.username)

                ProfileItem(profile, pets)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}

@Composable
fun ProfileItem(
    profile: UserProfile,
    pets: List<Pet>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(8.dp)
    ) {
        Text(text = "Username: ${profile.username}")
        Text(text = "Name: ${profile.name}")
        Text(text = "Bio: ${profile.bio}")

        if (pets.isNotEmpty()) {
            Text("Pets:")
            pets.forEach { pet ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray)
                    .padding(16.dp)
                ) {
                    Text("Name: ${pet.name}")
                    Text("Animal: ${pet.animal}")
                    Text("Age: ${pet.age}")
                    Text("Colour: ${pet.colour}")
                    Text("Breed: ${pet.breed}")
                }
            }
        } else {
            Text("This user does not have any pets", color = Color.Red)
        }
    }
}