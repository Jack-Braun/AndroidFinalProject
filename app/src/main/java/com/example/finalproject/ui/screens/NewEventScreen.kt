package com.example.finalproject.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finalproject.data.Event
import com.example.finalproject.data.Park

@Composable
fun EventScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    addEvent: (Event) -> Unit,
    fetchParks: suspend () -> List<Park>
) {
    var name by remember { mutableStateOf("") }
    var selectedAddress by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var parks by remember { mutableStateOf<List<Park>>(emptyList()) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        parks = fetchParks()
        Log.d("API", "parks loaded as: $parks")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("New Event")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        //Creates a dropdown menu to choose addresses from the api
        Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Button(onClick = { dropdownExpanded = !dropdownExpanded }) {
                Text(text = if (selectedAddress.isEmpty()) "Select Address" else selectedAddress)
            }
            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                parks.forEach { park ->
                    DropdownMenuItem(
                        text = { Text(park.address ?: "Unknown Address") },
                        onClick = {
                            selectedAddress = park.address ?: "Unknown Address"
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (name.isNotBlank() && selectedAddress.isNotBlank() && date.isNotBlank()) {
                    val event = Event(
                        id = 0,
                        name = name,
                        address = selectedAddress,
                        date = date,
                        attendees = mutableListOf()
                    )
                    addEvent(event)
                    onNavigateBack()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }
    }
}