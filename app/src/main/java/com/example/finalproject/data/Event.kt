package com.example.finalproject.data

data class Event(
    val id: Int,
    val name: String,
    val address: String,
    val date: String,
    val attendees: MutableList<String> = mutableListOf()
)
