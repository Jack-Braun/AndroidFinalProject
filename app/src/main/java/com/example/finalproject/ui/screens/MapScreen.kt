package com.example.finalproject.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.finalproject.ui.components.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    eventAddress: String? = "",
    viewModel: MapViewModel = MapViewModel()
) {

    val parks by viewModel.parks.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        //Base zoom coords
        position = CameraPosition.fromLatLngZoom(LatLng(49.2036, -122.9126), 10f)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchParks()
    }

    val eventCoords = parks.find{park ->
        park.address.equals(eventAddress)
    }
    eventCoords?.geo_point_2d?.let { coords ->
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            LatLng(coords.lat, coords.lon), 18f
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        parks.forEach { park ->
            val lat = park.geo_point_2d?.lat
            val lon = park.geo_point_2d?.lon
            val address = park.address
//            Log.d("MAP", "Lat: $lat, Lon: $lon, Address: $address")
            if (lat != null && lon != null) {
                if(eventAddress == address) {
                    Marker(
                        state = MarkerState(position = LatLng(lat, lon)),
                        title = "Your Events Address",
                        snippet = address
                    )
                } else {
                    Marker(
                        state = MarkerState(position = LatLng(lat, lon)),
                        title = "Dog Park",
                        snippet = address ?: "Address Not Found"
                    )
                }
            }
        }
    }
}
