package com.example.photogallery.data.remote

import com.example.photogallery.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET(Constants.API_URL)
    fun fetchPhotos(): Call<FlickrResponse>

    @GET(Constants.SEARCH_URL)
    fun searchPhotos(@Query("text") query: String): Call<FlickrResponse>
}
