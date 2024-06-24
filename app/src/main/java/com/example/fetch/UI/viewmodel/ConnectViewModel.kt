package com.example.fetch.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetch.data.model.User
import com.example.fetch.domain.GetUsersNearMe
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class ConnectViewModel: ViewModel() {

    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()

    var usersNearMe = GetUsersNearMe()
    var users = MutableLiveData<List<User>>()
    var appointment = MutableLiveData<String>()

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

    fun schedule(GeoPointStart: org.osmdroid.util.GeoPoint, geoPointEnd: org.osmdroid.util.GeoPoint, startingLocation:String, endingLocation:String, idWalker:String){
        val tomorrow = getTomorrowsDate()
        val map = hashMapOf(
            "startingLocation" to startingLocation,
            "endingLocation" to endingLocation,
            "userId" to auth.currentUser!!.uid,
            "idWalker" to idWalker,
            "status" to "pending",
            "date" to tomorrow,
            "latitudeStart" to GeoPointStart.latitude,
            "longitudeStart" to GeoPointStart.longitude,
            "latitudeEnd" to geoPointEnd.latitude,
            "longitudeEnd" to geoPointEnd.longitude,
            "createdAt" to Timestamp.now()
        )

        firestore.collection("Appointments").add(map).addOnSuccessListener {
            appointment.value = "Appointment scheduled successfully"
        }.addOnFailureListener { e ->
            appointment.value = "Error scheduling appointment: ${e.message}"
        }
    }

    fun getTomorrowsDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)
    }
}