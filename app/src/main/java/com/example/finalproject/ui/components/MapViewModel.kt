package com.example.finalproject.ui.components

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.data.Coords
import com.example.finalproject.data.Park
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {
    private val _parks = MutableStateFlow(emptyList<Park>())
    val parks: StateFlow<List<Park>> = _parks

    fun fetchParks() = viewModelScope.launch {
        try {
            val response = RetrofitInstance.api.getParks()
            _parks.value = response.results.mapNotNull { record ->
                val coords = record.geo_point_2d?.let {
                    Coords(
                        lon = shortenCoords(it.lon),
                        lat = shortenCoords(it.lat)
                    )
                }
                coords?.let {
                    Park(
                        address = record.address,
                        geo_point_2d = coords
                    )
                }
            }
            Log.d("MAP", "Response: ${response.results}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //This function is needed as the coords from the api are too long to function with google maps
    private fun shortenCoords(value: Double): Double {
        return "%.6f".format(value).toDouble()
    }
}