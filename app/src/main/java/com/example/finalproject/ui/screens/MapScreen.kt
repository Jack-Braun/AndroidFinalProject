package com.example.finalproject.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen() {
    val douglasCollege = LatLng(49.20363807155732, -122.91262245691598)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(douglasCollege, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        Marker(
            state = MarkerState(position = douglasCollege),
            title = "Douglas College",
//            snippet = "Marker placed in Douglas College"
        )
    }
}