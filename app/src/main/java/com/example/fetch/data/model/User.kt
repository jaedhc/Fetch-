package com.example.fetch.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val isWalker: Boolean,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double
)
