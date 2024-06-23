package com.example.fetch.data

import com.example.fetch.data.model.Dog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MyDgosRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getMyDogs(): List<Dog>{
        val user = auth.currentUser
        val myDogs = mutableListOf<Dog>()
        firestore.collection("dogs")
            .whereEqualTo("userId", user?.uid)
            .get()
            .addOnSuccessListener{ dogs ->
                dogs.forEach { dog ->
                    val name = dog.getString("name").toString()
                    val age = dog.getString("age").toString().toInt()
                    val breed = dog.getString("breed").toString()
                    val img = dog.getString("image").toString()
                    val desc = dog.getString("desc").toString()
                    val createdAt = dog.getLong("createdAt").toString().toLong()

                    val newDog = Dog(name, breed, age, img, desc, createdAt)
                    myDogs.add(newDog)
                }
            }.await()
        return myDogs
    }
}