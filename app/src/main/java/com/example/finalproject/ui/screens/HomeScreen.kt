package com.example.finalproject.ui.screens

import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalproject.R
import com.example.finalproject.data.Pet
import com.example.finalproject.data.UserProfile
import com.example.finalproject.getUserPets
import com.example.finalproject.ui.components.screens
import com.example.finalproject.ui.theme.FinalProjectTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit,
    profiles: List<UserProfile>
) {
    var droppedDown by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { droppedDown = !droppedDown },
                modifier = Modifier
            ) {
                Text("Navigation")
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            }
        }

        DropdownMenu(
            expanded = droppedDown,
            onDismissRequest = { droppedDown = false }
        ) {
            screens.forEach { screen ->
                DropdownMenuItem(
                    onClick = {
                        droppedDown = false
                        onNavigate(screen.name)
                    },
                    text = {
                        Text(
                            text = stringResource(screen.title),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }
        }

        Text(text = "Pet App", fontSize = 24.sp)
        Image(
            painter = painterResource(id = R.drawable.dog_logo),
            contentDescription = "A Dog Logo",
            modifier = Modifier
                .size(192.dp)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("Other Users")
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(profiles) {profile ->
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
    pets: List<Pet>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "Username: ${profile.username}")
        Text(text = "Name: ${profile.name}")
        Text(text = "Bio: ${profile.bio}")
        if(pets.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Pets:")
            pets.forEach {pet ->
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