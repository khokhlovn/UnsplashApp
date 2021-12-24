package com.example.unsplashapp.api.resp

import com.google.gson.annotations.SerializedName

class CollectionsResponse : ArrayList<CollectionsResponse.CollectionsResponseItem>() {
    data class CollectionsResponseItem(
        @SerializedName("cover_photo")
        val coverPhoto: CoverPhoto,
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("last_collected_at")
        val lastCollectedAt: String,
        @SerializedName("links")
        val links: Links,
        @SerializedName("private")
        val `private`: Boolean,
        @SerializedName("published_at")
        val publishedAt: String,
        @SerializedName("share_key")
        val shareKey: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("total_photos")
        val totalPhotos: Int,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user")
        val user: User
    ) {
        data class CoverPhoto(
            @SerializedName("blur_hash")
            val blurHash: String,
            @SerializedName("color")
            val color: String,
            @SerializedName("description")
            val description: String,
            @SerializedName("height")
            val height: Int,
            @SerializedName("id")
            val id: String,
            @SerializedName("liked_by_user")
            val likedByUser: Boolean,
            @SerializedName("likes")
            val likes: Int,
            @SerializedName("links")
            val links: Links,
            @SerializedName("urls")
            val urls: Urls,
            @SerializedName("user")
            val user: User,
            @SerializedName("width")
            val width: Int
        ) {
            data class Links(
                @SerializedName("download")
                val download: String,
                @SerializedName("html")
                val html: String,
                @SerializedName("self")
                val self: String
            )

            data class Urls(
                @SerializedName("full")
                val full: String,
                @SerializedName("raw")
                val raw: String,
                @SerializedName("regular")
                val regular: String,
                @SerializedName("small")
                val small: String,
                @SerializedName("thumb")
                val thumb: String
            )

            data class User(
                @SerializedName("bio")
                val bio: String,
                @SerializedName("id")
                val id: String,
                @SerializedName("links")
                val links: Links,
                @SerializedName("location")
                val location: String,
                @SerializedName("name")
                val name: String,
                @SerializedName("portfolio_url")
                val portfolioUrl: String,
                @SerializedName("profile_image")
                val profileImage: ProfileImage,
                @SerializedName("total_collections")
                val totalCollections: Int,
                @SerializedName("total_likes")
                val totalLikes: Int,
                @SerializedName("total_photos")
                val totalPhotos: Int,
                @SerializedName("username")
                val username: String
            ) {
                data class Links(
                    @SerializedName("html")
                    val html: String,
                    @SerializedName("likes")
                    val likes: String,
                    @SerializedName("photos")
                    val photos: String,
                    @SerializedName("portfolio")
                    val portfolio: String,
                    @SerializedName("self")
                    val self: String
                )

                data class ProfileImage(
                    @SerializedName("large")
                    val large: String,
                    @SerializedName("medium")
                    val medium: String,
                    @SerializedName("small")
                    val small: String
                )
            }
        }

        data class Links(
            @SerializedName("html")
            val html: String,
            @SerializedName("photos")
            val photos: String,
            @SerializedName("related")
            val related: String,
            @SerializedName("self")
            val self: String
        )

        data class User(
            @SerializedName("bio")
            val bio: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("links")
            val links: Links,
            @SerializedName("location")
            val location: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("portfolio_url")
            val portfolioUrl: String,
            @SerializedName("profile_image")
            val profileImage: ProfileImage,
            @SerializedName("total_collections")
            val totalCollections: Int,
            @SerializedName("total_likes")
            val totalLikes: Int,
            @SerializedName("total_photos")
            val totalPhotos: Int,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("username")
            val username: String
        ) {
            data class Links(
                @SerializedName("html")
                val html: String,
                @SerializedName("likes")
                val likes: String,
                @SerializedName("photos")
                val photos: String,
                @SerializedName("portfolio")
                val portfolio: String,
                @SerializedName("self")
                val self: String
            )

            data class ProfileImage(
                @SerializedName("large")
                val large: String,
                @SerializedName("medium")
                val medium: String,
                @SerializedName("small")
                val small: String
            )
        }
    }
}
