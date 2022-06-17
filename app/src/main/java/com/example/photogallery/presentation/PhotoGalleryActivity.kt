package com.example.photogallery.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.photogallery.R
import com.example.photogallery.presentation.photoGallery.PhotoGalleryFragment

class PhotoGalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)
        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, PhotoGalleryFragment.newInstance())
                .commit()
        }
    }
}