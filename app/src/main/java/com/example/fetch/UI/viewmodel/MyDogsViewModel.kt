package com.example.fetch.UI.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetch.data.model.Dog
import com.example.fetch.domain.GetMyDogs
import kotlinx.coroutines.launch

class MyDogsViewModel: ViewModel() {
    var dogList = MutableLiveData<List<Dog>>()

    var getMyDogs = GetMyDogs()

    fun onCreate(){
        viewModelScope.launch {
            val myDogs = getMyDogs()
            dogList.postValue(myDogs)
        }
    }
}