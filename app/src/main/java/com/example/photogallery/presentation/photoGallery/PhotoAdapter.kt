package com.example.photogallery.presentation.photoGallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photogallery.PhotoPageActivity
import com.example.photogallery.R
import com.example.photogallery.data.model.GalleryItem

class PhotoAdapter(
    private val galleryItem: List<GalleryItem>,
    private val context: Context,
) :
    RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    class PhotoHolder(itemImageView: ImageView, val context: Context) :
        RecyclerView.ViewHolder(itemImageView),
        View.OnClickListener {
        private lateinit var galleryItem: GalleryItem

        init {
            itemView.setOnClickListener(this)
        }

        fun bindGalleryItem(item: GalleryItem) {
            galleryItem = item
            Glide.with(context).load(galleryItem.url).placeholder(R.drawable.hide_image)
                .into(itemView as ImageView)
        }

        override fun onClick(view: View) {
            val intent = PhotoPageActivity.newIntent(context, galleryItem.photoPageUri)
            view.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_gallery, parent, false) as ImageView
        return PhotoHolder(view, context)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val galleryItem = galleryItem[position]
        holder.bindGalleryItem(galleryItem)
    }

    override fun getItemCount(): Int = galleryItem.size
}
