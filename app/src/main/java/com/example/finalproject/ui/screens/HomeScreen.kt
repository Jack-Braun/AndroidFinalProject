package com.example.finalproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalproject.R
import com.example.finalproject.ui.components.screens
import com.example.finalproject.ui.theme.FinalProjectTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
) {
    var droppedDown by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { droppedDown = !droppedDown },
            //CHANGE THE WAY ITS CENTERED
            modifier = Modifier
                .padding(start = 210.dp)
        ) {
            Text("Navigation")
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
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
                            modifier = modifier.widthIn(min = 250.dp)
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


    }
}

//@Composable
//fun NavigateScreens(
//    @StringRes labelResourceId: Int,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    DropdownMenuItem(
//        onClick = onClick,
//        text = {
//            Text(
//                text = stringResource(labelResourceId),
//                modifier = modifier.widthIn(min = 250.dp)
//            )
//        }
//    )
//}

@Preview
@Composable
fun HomePreview() {
    FinalProjectTheme {
        HomeScreen(onNavigate = {})
    }
}