package com.example.photogallery

import android.app.Application
import com.example.photogallery.data.remote.FlickrApi
import com.example.photogallery.data.remote.PhotoInterceptor
import com.example.photogallery.data.repository.PhotoGalleryRepository
import com.example.photogallery.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotoGalleryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val client = OkHttpClient.Builder().addInterceptor(PhotoInterceptor()).build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        val flickrApi = retrofit.create(FlickrApi::class.java)
        PhotoGalleryRepository.initialize(flickrApi)
    }
}
