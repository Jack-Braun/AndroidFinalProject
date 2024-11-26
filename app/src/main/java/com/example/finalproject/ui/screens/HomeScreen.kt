package com.example.finalproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalproject.R
import com.example.finalproject.data.Event
import com.example.finalproject.data.Pet
import com.example.finalproject.data.UserProfile
import com.example.finalproject.getUserPets
import com.example.finalproject.ui.components.screens

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit,
    profiles: List<UserProfile>,
    events: List<Event>,
    newEvent: () -> Unit,
    currentUserProfile: UserProfile?,
    addUserToEventAction: (eventId: Int, username: String) -> Unit,
    onNavigateToMap: (String) -> Unit
) {
    var updatedEvents by remember { mutableStateOf(events) }
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
        Button(
            onClick = newEvent,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Create Event")
        }
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            items(updatedEvents) { event ->
                EventItem(
                    event = event,
                    currentUserProfile = currentUserProfile,
                    addUserToEvent = { eventId, username ->
                        addUserToEventAction(eventId, username)
                        updatedEvents = updatedEvents.map {
                            if (it.id == eventId) it.copy(attendees = (it.attendees + username).toMutableList())
                            else it
                        }
                    },
                    onNavigateToMap = {address ->
                        onNavigateToMap(address)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text("Other Users")
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
fun EventItem(
    event: Event,
    currentUserProfile: UserProfile?,
    addUserToEvent: (eventId: Int, username: String) -> Unit,
    onNavigateToMap: (String) -> Unit
) {
    val isUserAlreadyAttending = event.attendees.contains(currentUserProfile?.username)
    var updatedEvent by remember { mutableStateOf(event) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Event Name: ${event.name}")
        Text(text = "Address: ${event.address}")
        Button(
            onClick = {
                onNavigateToMap(event.address)
            }
        ) {
            Text("View This Location")
        }
        Text(text = "Date: ${event.date}")
        Spacer(modifier = Modifier.height(8.dp))

        if (event.attendees.isNotEmpty()) {
            Text("Attendees:")
            event.attendees.forEach { attendee ->
                Text(text = "- $attendee")
            }
        } else {
            Text("No attendees yet.")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                addUserToEvent(event.id, currentUserProfile?.username ?: "")
                updatedEvent = updatedEvent.copy(attendees = (updatedEvent.attendees + (currentUserProfile?.username ?: "")).toMutableList())
            },
            enabled = !isUserAlreadyAttending && currentUserProfile != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            if(isUserAlreadyAttending) {
                Text(text = "Already Signed Up")
            } else {
                Text(text = "Sign Up For This Event")
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
            .padding(8.dp)
    ) {
        Text(text = "Username: ${profile.username}")
        Text(text = "Name: ${profile.name}")
        Text(text = "Bio: ${profile.bio}")

        if (pets.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
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