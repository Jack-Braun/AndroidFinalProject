package com.example.finalproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.finalproject.isUsernameTaken

@Composable
fun RegisterScreen(
    register: (String, String, String, String) -> Unit
) {
    var usernameReg by remember { mutableStateOf("") }
    var passwordReg by remember { mutableStateOf("") }
    var nameReg by remember { mutableStateOf("") }
    var bioReg by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Pet App Register")

        OutlinedTextField(
            value = usernameReg,
            onValueChange = {
                usernameReg = it
                error = ""
            },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = passwordReg,
            onValueChange = {
                passwordReg = it
                error = ""
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = nameReg,
            onValueChange = {
                nameReg = it
                error = ""
            },
            label = { Text("Name(optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = bioReg,
            onValueChange = {
                bioReg = it
                error = ""
            },
            label = { Text("Bio(optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                if (usernameReg.isNotBlank() && passwordReg.isNotBlank()) {
                    if (isUsernameTaken(context, usernameReg)) {
                        error = "Username already exists."
                    } else {
                        register(usernameReg, passwordReg, nameReg, bioReg)
                    }
                } else {
                    error = "All fields are required."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}