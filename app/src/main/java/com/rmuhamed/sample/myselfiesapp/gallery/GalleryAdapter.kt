package com.rmuhamed.sample.myselfiesapp.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rmuhamed.sample.myselfiesapp.R
import com.rmuhamed.sample.myselfiesapp.api.dto.ImageDTO

class GalleryAdapter(private val images: List<ImageDTO>) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val itemContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_item, parent, false)

        return GalleryViewHolder(itemContainer)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(images[position])
    }

    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ImageDTO) {
            val imageView = itemView.findViewById<AppCompatImageView>(R.id.gallery_image_item)
            Glide.with(itemView.context).load(item.link).into(imageView);
        }
    }
}