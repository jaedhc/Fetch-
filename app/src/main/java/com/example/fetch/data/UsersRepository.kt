package com.example.fetch.data

import com.example.fetch.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UsersRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getUsersNearMe(usrLatitude: Double, uSrLongitude: Double): List<User> {
        val usersList = mutableListOf<User>()

        firestore.collection("Users").whereEqualTo("isWalker", true).get().addOnSuccessListener { users ->
            users.forEach { user ->
                val latitude = user.getDouble("latitude").toString().toDouble()
                val longitude = user.getDouble("longitude").toString().toDouble()

                if(areUsersNear(usrLatitude, uSrLongitude, latitude, longitude) == false){
                    val id = user.getString("userId").toString()
                    val name = user.getString("name").toString()
                    val email = user.getString("email").toString()
                    val isWalker = user.getBoolean("isWalker").toString().toBoolean()
                    val imageUrl = user.getString("photoURL").toString()

                    val usr = User(id, name, email, isWalker, imageUrl, latitude, longitude)
                    usersList.add(usr)
                }
            }
        }.await()

        return usersList
    }

    fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371e3 // Radio de la tierra en kilómetros
        val d1 = Math.toRadians(lat1)
        val d2 = Math.toRadians(lat2)
        val ad = Math.toRadians(lat1 - lat2)
        val bd = Math.toRadians(lon1 - lon2)

        val a = Math.sin(ad / 2) * Math.sin(ad / 2) + Math.cos(d1) * Math.cos(d2) * Math.sin(bd / 2) * Math.sin(bd / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return R * c
    }

    fun areUsersNear(userLatitude: Double, userLongitude: Double, latitude: Double, longitude: Double): Boolean{
        val distance = haversine(userLatitude, userLongitude, latitude, longitude)
        val bool = distance <= 5000.0
        return bool
    }

}