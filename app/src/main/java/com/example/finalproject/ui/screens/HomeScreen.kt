package com.example.finalproject.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.finalproject.ui.theme.FinalProjectTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Text("Hello")
}

@Preview
@Composable
fun HomePreview() {
    FinalProjectTheme {
        HomeScreen()
    }
}