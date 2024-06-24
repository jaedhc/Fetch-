package com.example.fetch.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetch.data.model.User
import com.example.fetch.domain.GetUsersNearMe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ConnectViewModel: ViewModel() {

    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()

    var usersNearMe = GetUsersNearMe()
    var users = MutableLiveData<List<User>>()

    fun changeLocation(longitude: Double, latitude: Double){
        val user = auth.currentUser

        if (user != null) {
            val userRef = firestore.collection("Users").document(user.uid)
            userRef.update("longitude", longitude, "latitude", latitude)
        }
    }

    fun getUsers(longitude: Double, latitude: Double){
        viewModelScope.launch {
            users.value = usersNearMe(longitude, latitude)
        }
    }
}