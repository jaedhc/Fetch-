package com.example.fetch.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class LogInViewModel: ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    var loginState: MutableLiveData<String> = MutableLiveData()

    fun logIn(email:String, pass:String){
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if(it.isSuccessful){
                loginState.value = "Logged"
            } else {
                loginState.value = "${it.exception}"
            }
        }
    }
}