package com.example.finalproject.data

data class Park(
    val address: String?,
    val geo_point_2d: Coords?
)

data class ParkResponse(
    val results: List<Park>
)

data class Coords(
    val lon: Double,
    val lat: Double
)