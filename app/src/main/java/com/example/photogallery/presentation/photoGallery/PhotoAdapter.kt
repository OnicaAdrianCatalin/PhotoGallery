package com.example.photogallery.presentation.photoGallery

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.photogallery.R
import com.example.photogallery.ThumbnailDownloader
import com.example.photogallery.data.model.GalleryItem

class PhotoAdapter(
    private val galleryItem: List<GalleryItem>,
    private val context: Context,
    private val thumbnailDownloader: ThumbnailDownloader<PhotoHolder>
) :
    RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    class PhotoHolder(itemImageView: ImageView) : RecyclerView.ViewHolder(itemImageView) {

        val bindDrawable: (Drawable) -> Unit = itemImageView::setImageDrawable
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_gallery, parent, false) as ImageView
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val galleryItem = galleryItem[position]
        val placeholder: Drawable =
            ContextCompat.getDrawable(context, R.drawable.bill_up_close) ?: ColorDrawable()
        holder.bindDrawable(placeholder)
        thumbnailDownloader.queueThumbnail(holder, galleryItem.url)
    }

    override fun getItemCount(): Int = galleryItem.size
}
