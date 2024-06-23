package com.example.fetch.domain

import com.example.fetch.data.MyDgosRepository
import com.example.fetch.data.model.Dog

class GetMyDogs {

    private val repository = MyDgosRepository()

    suspend operator fun invoke(): List<Dog> {
        return repository.getMyDogs()
    }

}