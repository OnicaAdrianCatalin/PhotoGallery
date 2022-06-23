package com.example.photogallery.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photogallery.data.model.GalleryItem
import com.example.photogallery.data.remote.FlickrApi
import com.example.photogallery.data.remote.FlickrResponse
import com.example.photogallery.data.remote.PhotoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoGalleryRepository(private val flickrApi: FlickrApi) {

    fun fetchPhotosRequest(): Call<FlickrResponse> {
        return flickrApi.fetchPhotos()
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        return fetchPhotosMetadata(fetchPhotosRequest())
    }

    fun searchPhotosRequest(query: String): Call<FlickrResponse> {
        return flickrApi.searchPhotos(query)
    }

    fun searchPhotos(query: String): LiveData<List<GalleryItem>> {
        return fetchPhotosMetadata(searchPhotosRequest(query))
    }

    private fun fetchPhotosMetadata(flickrRequest: Call<FlickrResponse>): LiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()

        flickrRequest.enqueue(object : Callback<FlickrResponse> {

            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(
                call: Call<FlickrResponse>,
                response: Response<FlickrResponse>
            ) {
                Log.d(TAG, "Response received")
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems
                    ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = galleryItems
            }
        })
        return responseLiveData
    }

    companion object {
        private const val TAG = "FlickrRepository"
        private var INSTANCE: PhotoGalleryRepository? = null

        fun initialize(flickrApi: FlickrApi) {
            if (INSTANCE == null) {
                INSTANCE = PhotoGalleryRepository(flickrApi)
            }
        }

        fun get(): PhotoGalleryRepository {
            return INSTANCE ?: throw IllegalStateException("FlickrRepository must be initialized")
        }
    }
}
