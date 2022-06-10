package com.example.photogallery.data.remote

import com.example.photogallery.utils.Constants
import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {
    @GET(Constants.API_URL)
    fun fetchPhotos(): Call<FlickrResponse>
}
