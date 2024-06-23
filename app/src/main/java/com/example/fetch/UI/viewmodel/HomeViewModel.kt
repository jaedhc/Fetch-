package com.example.fetch.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel(){
    var isLoading = MutableLiveData<Boolean>()
    var user = MutableLiveData<String>()
    var userImg = MutableLiveData<String>()
    private var auth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()

    fun getUserData(){ isLoading.value = true
        auth = FirebaseAuth.getInstance()
        firestore.collection("Users").document(auth.currentUser!!.uid).get().addOnCompleteListener{
           if(it.isSuccessful){
               user.value = it.result.get("name").toString()
               userImg.value = it.result.get("photoURL").toString()
           } else {
               user.value = "Error"
           }
           isLoading.value = false
       }
    }
}