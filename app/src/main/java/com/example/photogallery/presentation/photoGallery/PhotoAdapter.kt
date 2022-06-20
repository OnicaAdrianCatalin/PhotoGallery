package com.example.photogallery.presentation.photoGallery

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photogallery.R
import com.example.photogallery.data.model.GalleryItem

class PhotoAdapter(
    private val galleryItem: List<GalleryItem>,
    private val context: Context,
) :
    RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    class PhotoHolder(itemImageView: ImageView) :
        RecyclerView.ViewHolder(itemImageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_gallery, parent, false) as ImageView
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val galleryItem = galleryItem[position]
        Glide.with(context).load(galleryItem.url).placeholder(R.drawable.hide_image)
            .into(holder.itemView as ImageView)
    }

    override fun getItemCount(): Int = galleryItem.size
}
