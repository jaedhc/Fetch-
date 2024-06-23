package com.example.fetch.UI.viewmodel


import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetch.UI.view.HomeActivity
import com.example.fetch.data.model.Breed
import com.example.fetch.domain.GetBreeds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AddPuppyViewModel : ViewModel(){

    val breedModel = MutableLiveData<List<Breed>>()
    val isLoading = MutableLiveData<Boolean>()
    val created = MutableLiveData<String>()

    private var firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var auth = FirebaseAuth.getInstance()

    var getBreeds = GetBreeds()

    fun onCreate(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val breeds = getBreeds()
            if(breeds != null){
                breedModel.postValue(breeds)
                isLoading.postValue(false)
            } else {
                Log.d("Fortnite", "Error")
            }
        }
    }

    fun addPuppy(img:String, breed: String, name: String, age: String, description: String) {
        val userId = auth.currentUser?.uid
        val dog = hashMapOf(
            "breed" to breed,
            "name" to name,
            "age" to age,
            "description" to description,
            "userId" to userId,
            "image" to img,
            "createdAt" to System.currentTimeMillis()
        )
        firestore.collection("dogs")
            .add(dog).addOnCompleteListener {
                if(it.isSuccessful){
                    created.value = "Created"
                } else {
                    created.value = "${it.exception}"
                }
            }
    }

    suspend fun uploadImg(id:String, uri: Uri): String{
           return try {
               val uniqueId = UUID.randomUUID().toString()
               val ref = storage.reference.child("imagesDogs/$uniqueId")
               ref.putFile(uri).await()
               ref.downloadUrl.await().toString()
           } catch (e: Exception){
                "https://firebasestorage.googleapis.com/v0/b/fetch-773cc.appspot.com/o/puppy.jpg?alt=media&token=58697f26-1e7c-44a9-bc6c-8a56bef3677c"
           }
    }

}