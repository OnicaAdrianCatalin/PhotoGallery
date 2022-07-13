package com.example.photogallery.data.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class GalleryItem(
    var title: String = "",
    var id: String = "",
    @SerializedName("url_s") var url: String = "",
    @SerializedName("owner") val owner: String = ""
) {

    val photoPageUri: Uri
        get() {
            return Uri.parse(URL)
                .buildUpon()
                .appendPath(owner)
                .build()
        }
    companion object{
        private const val URL = "https://www.flickr.com/photos/"
    }
}
