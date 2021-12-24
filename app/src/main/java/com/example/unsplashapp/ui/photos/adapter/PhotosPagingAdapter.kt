package com.example.unsplashapp.ui.photos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.unsplashapp.R
import com.example.unsplashapp.api.resp.PhotosResponse
import com.example.unsplashapp.databinding.ItemPhotoBinding

class PhotosPagingAdapter :
    PagingDataAdapter<PhotosResponse.PhotosResponseItem, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    lateinit var listener: (
        String, String, String, Int, Boolean,
        String, String, String, String, Int, Int
    ) -> Unit

    companion object {
        private val REPO_COMPARATOR =
            object : DiffUtil.ItemCallback<PhotosResponse.PhotosResponseItem>() {
                override fun areItemsTheSame(
                    oldItem: PhotosResponse.PhotosResponseItem,
                    newItem: PhotosResponse.PhotosResponseItem
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: PhotosResponse.PhotosResponseItem,
                    newItem: PhotosResponse.PhotosResponseItem
                ): Boolean =
                    oldItem.urls.full == oldItem.urls.full
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PhotosViewHolder)?.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotosViewHolder(itemBinding)
    }

    inner class PhotosViewHolder(private val itemBinding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: PhotosResponse.PhotosResponseItem?) {
            if (item != null) {
                itemBinding.authorName.text = item.user.name
                Glide
                    .with(itemBinding.root)
                    .load(item.user.profileImage.medium)
                    .placeholder(R.drawable.placeholder)
                    .circleCrop()
                    .into(itemBinding.authorPhoto)

                Glide
                    .with(itemBinding.root)
                    .load(item.urls.regular)
                    .placeholder(R.drawable.placeholder)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(8)))
                    .into(itemBinding.mediaImage)

                itemBinding.mediaImage.setOnClickListener {
                    listener.invoke(
                        item.links.html, item.id, item.user.name, item.likes,
                        item.likedByUser, item.user.profileImage.medium, item.urls.raw,
                        item.urls.regular, item.createdAt, item.width, item.height
                    )
                }
            }
        }
    }
}
