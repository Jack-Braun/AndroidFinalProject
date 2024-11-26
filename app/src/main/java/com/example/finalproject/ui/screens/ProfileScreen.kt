package com.example.finalproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.finalproject.data.UserProfile

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    logout: () -> Unit,
    onEditProfile: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Sharp.AccountCircle,
            contentDescription = "Profile Picture"
        )
        Text("Username: ${userProfile.username}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Text("Full Name: ${userProfile.name}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Text("Bio: ${userProfile.bio}", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

        Button(onClick = onEditProfile, modifier = Modifier.fillMaxWidth()) {
            Text("Edit Profile")
        }
        Button(onClick = logout, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}