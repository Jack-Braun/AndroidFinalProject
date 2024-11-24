package com.example.finalproject.ui.screens

import android.content.Context
import android.net.Uri
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

@Composable
fun LoginScreen(
    login: (String, String) -> Unit,
    register: () -> Unit,
) {
    var username by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("") }
    var incorrectLogin by remember{ mutableStateOf(false) }
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
       Text("Pet App Login")

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        if(incorrectLogin) {
            Text(text = "Invalid Login", color = Color.Red)
        }

        Button(
            onClick = {
                if (validateLogin(context, username, password)) {
                    incorrectLogin = false
                    login(username, password)
                } else {
                    incorrectLogin = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                register()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}

fun validateLogin(context: Context, username: String, password: String): Boolean {
    val uri = Uri.parse("content://com.example.finalproject/users")
    val projection = arrayOf("username", "password")
    val selection = "username = ? AND password = ?"
    val selectionArgs = arrayOf(username, password)

    val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
    val isValid = (cursor?.count ?: 0) > 0
    cursor?.close()
    return isValid
}