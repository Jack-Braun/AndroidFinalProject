package com.example.finalproject.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.finalproject.ui.components.screens
import com.example.finalproject.ui.theme.FinalProjectTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        screens.forEach { screen ->
            NavigateScreens(
                labelResourceId = screen.title,
                route = screen.name,
                onClick = onNavigate
            )
        }
    }
}

@Composable
fun NavigateScreens(
    @StringRes labelResourceId: Int,
    onClick: (String) -> Unit,
    route: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {onClick(route)},
        modifier = modifier.widthIn(min = 250.dp)
    ) {
        Text(stringResource(labelResourceId))
    }
}

@Preview
@Composable
fun HomePreview() {
    FinalProjectTheme {
        HomeScreen(onNavigate = {})
    }
}