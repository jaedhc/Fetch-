package com.example.fetch.domain

import com.example.fetch.data.BreedsRepository
import com.example.fetch.data.model.Breed
import com.example.fetch.data.model.BreedsResponse

class GetBreeds {

    private val repository = BreedsRepository()

    suspend operator fun invoke(): List<Breed> {
        return repository.getAllBreeds()
    }

}