package com.example.photogallery.presentation.photoGallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.photogallery.data.QueryPreferences
import com.example.photogallery.data.model.GalleryItem
import com.example.photogallery.data.repository.PhotoGalleryRepository

class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {
    private val flickrRepository = PhotoGalleryRepository.get()
    private val mutableSearchTerm = MutableLiveData<String>()
    val searchTerm: String
        get() = mutableSearchTerm.value ?: ""
    val galleryItemLiveData: LiveData<List<GalleryItem>> =
        Transformations.switchMap(mutableSearchTerm) { searchTerm ->
            if (searchTerm.isBlank()) {
                flickrRepository.fetchPhotos()
            } else {
                flickrRepository.searchPhotos(searchTerm)
            }
        }

    init {
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }
}
