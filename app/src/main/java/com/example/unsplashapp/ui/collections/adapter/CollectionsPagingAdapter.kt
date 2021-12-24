package com.example.unsplashapp.ui.collections.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.unsplashapp.R
import com.example.unsplashapp.api.resp.CollectionsResponse
import com.example.unsplashapp.databinding.ItemCollectionBinding

class CollectionsPagingAdapter :
    PagingDataAdapter<CollectionsResponse.CollectionsResponseItem, RecyclerView.ViewHolder>(
        REPO_COMPARATOR
    ) {

    lateinit var listener: (String, String, String?, Int, String) -> Unit

    companion object {
        private val REPO_COMPARATOR =
            object : DiffUtil.ItemCallback<CollectionsResponse.CollectionsResponseItem>() {
                override fun areItemsTheSame(
                    oldItem: CollectionsResponse.CollectionsResponseItem,
                    newItem: CollectionsResponse.CollectionsResponseItem
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: CollectionsResponse.CollectionsResponseItem,
                    newItem: CollectionsResponse.CollectionsResponseItem
                ): Boolean =
                    oldItem.links.photos == oldItem.links.photos
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? CollectionsViewHolder)?.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemCollectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectionsViewHolder(itemBinding)
    }

    inner class CollectionsViewHolder(private val itemBinding: ItemCollectionBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: CollectionsResponse.CollectionsResponseItem?) {
            if (item != null) {
                itemBinding.authorName.text = item.user.name
                itemBinding.title.text = item.title
                itemBinding.count.text = itemBinding.root.context.getString(
                    R.string.photos_count,
                    item.totalPhotos
                )

                Glide
                    .with(itemBinding.root)
                    .load(item.user.profileImage.medium)
                    .placeholder(R.drawable.placeholder)
                    .circleCrop()
                    .into(itemBinding.authorPhoto)

                Glide
                    .with(itemBinding.root)
                    .load(item.coverPhoto.urls.regular)
                    .placeholder(R.drawable.placeholder)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(8)))
                    .into(itemBinding.mediaImage)

                itemBinding.root.setOnClickListener {
                    listener.invoke(
                        item.id, item.title, item.description,
                        item.totalPhotos, item.user.name
                    )
                }
            }
        }
    }
}
