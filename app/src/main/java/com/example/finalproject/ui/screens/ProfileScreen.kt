package com.example.finalproject.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.finalproject.data.UserProfile

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userProfile: UserProfile,
    logout: () -> Unit,
    onEditProfile: () -> Unit
) {
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Sharp.AccountCircle,
            contentDescription = "Profile Picture"
        )
        Text("Username: ${userProfile.username}")
        Text("Full Name: ${userProfile.name}")
        Text("Bio: ${userProfile.bio}")

        Button(onClick = onEditProfile) {
            Text("Edit Profile")
        }
        Button(onClick = logout) {
            Text("Logout")
        }
    }
}