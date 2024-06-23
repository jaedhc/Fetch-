package com.example.fetch.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://api.doggiepedia.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}