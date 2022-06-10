package com.example.photogallery.presentation.photoGallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.photogallery.data.model.GalleryItem
import com.example.photogallery.data.repository.PhotoGalleryRepository

class PhotoGalleryViewModel : ViewModel() {
    private val flickrRepository = PhotoGalleryRepository.get()
    val galleryItemLiveData: LiveData<List<GalleryItem>> = flickrRepository.fetchPhotos()
}
