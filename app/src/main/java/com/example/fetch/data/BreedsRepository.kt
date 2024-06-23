package com.example.fetch.data

import com.example.fetch.data.model.Breed
import com.example.fetch.data.model.BreedProvider
import com.example.fetch.data.model.BreedsResponse
import com.example.fetch.data.network.BreedService

class BreedsRepository {

    private val breedService = BreedService()

    suspend fun getAllBreeds(): List<Breed>{
        var breeds = mutableListOf<Breed>()
        for (page in 1..45){
            val response = breedService.getBreeds(page)
            BreedProvider.breeds.addAll(response.breeds)
            breeds = BreedProvider.breeds
        }
        return breeds as List<Breed>
    }
}