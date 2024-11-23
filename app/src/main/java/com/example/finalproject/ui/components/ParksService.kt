package com.example.finalproject.ui.components

import com.example.finalproject.data.ParkResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ParksService {
    //for some reason i need to manually input the max limit of data or else it only returns 9 values
    @GET("/api/explore/v2.1/catalog/datasets/dog-off-leash-parks/records?limit=44")
    suspend fun getParks(): ParkResponse
}

object RetrofitInstance {
    val api: ParksService by lazy {
        Retrofit.Builder()
            .baseUrl("https://opendata.vancouver.ca/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ParksService::class.java)
    }
}