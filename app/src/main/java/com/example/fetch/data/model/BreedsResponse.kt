package com.example.fetch.data.model

import com.google.gson.annotations.SerializedName

data class BreedsResponse(
    @SerializedName("all_pages") val all_pages: Int,
    @SerializedName("breeds") val breeds: List<Breed>
)
