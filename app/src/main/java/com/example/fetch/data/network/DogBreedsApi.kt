package com.example.fetch.data.network

import com.example.fetch.data.model.Breed
import retrofit2.http.GET
import com.example.fetch.data.model.BreedsResponse
import retrofit2.Response
import retrofit2.http.Query

interface DogBreedsApi {
    @GET("http://api.doggiepedia.org/all")
    suspend fun getBreeds(@Query("page") page: Int): Response<BreedsResponse>
}