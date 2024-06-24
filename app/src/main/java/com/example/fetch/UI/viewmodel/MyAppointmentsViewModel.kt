package com.example.fetch.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetch.data.model.Appointment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MyAppointmentsViewModel : ViewModel() {

    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    var appointments = MutableLiveData<List<Appointment>>()

    fun getAppointments() {
        val appointmentsList = mutableListOf<Appointment>()
        viewModelScope.launch {
            firestore.collection("Appointments").whereEqualTo("userId", auth.currentUser!!.uid).get().addOnSuccessListener {
                it.forEach { app ->

                    val location = app.get("startingLocation") as String
                    val endingLocation = app.get("endingLocation") as String
                    val date = app.get("date") as String
                    val idWalker = app.get("idWalker") as String
                    val status = app.get("status") as String
                    val userId = app.get("userId") as String
                    val latitudeStart = app.get("latitudeStart") as Double
                    val longitudeStart = app.get("longitudeStart") as Double
                    val latitudeEnd = app.get("latitudeEnd") as Double
                    val longitudeEnd = app.get("longitudeEnd") as Double

                    val apps = Appointment(location, endingLocation, userId, idWalker, status, date, latitudeStart, longitudeStart, latitudeEnd, longitudeEnd)

                    appointmentsList.add(apps)
                }
            }.await()
            appointments.value = appointmentsList
        }
    }
}