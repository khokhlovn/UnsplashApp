package com.example.unsplashapp.api.resp

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val results: ArrayList<PhotosResponse.PhotosResponseItem>
)
