package com.example.fetch.data.model

data class Appointment(
    val startingLocation: String,
    val endingLocation: String,
    val userId: String,
    val idWalker: String,
    val status: String,
    val date: String,
    val latitudeStart: Double,
    val longitudeStart: Double,
    val latitudeEnd: Double,
    val longitudeEnd: Double,
)
