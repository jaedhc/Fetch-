package com.example.fetch.data.network

import com.example.fetch.core.RetrofitHelper
import com.example.fetch.data.model.BreedsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


class BreedService {

    private val retrofit = RetrofitHelper().getRetrofit()

    suspend fun getBreeds(page: Int):BreedsResponse{
        return withContext(Dispatchers.IO){
            var response: Response<BreedsResponse> = retrofit.create(DogBreedsApi::class.java).getBreeds(page)
            response.body() ?: BreedsResponse(0, emptyList())
        }
    }

}