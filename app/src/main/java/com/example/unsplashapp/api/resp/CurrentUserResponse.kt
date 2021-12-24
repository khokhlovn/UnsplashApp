package com.example.unsplashapp.api.resp

import com.google.gson.annotations.SerializedName

data class CurrentUserResponse(
    @SerializedName("bio")
    val bio: String,
    @SerializedName("downloads")
    val downloads: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("followed_by_user")
    val followedByUser: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("instagram_username")
    val instagramUsername: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("links")
    val links: Links,
    @SerializedName("location")
    val location: Any,
    @SerializedName("portfolio_url")
    val portfolioUrl: Any,
    @SerializedName("total_collections")
    val totalCollections: Int,
    @SerializedName("total_likes")
    val totalLikes: Int,
    @SerializedName("total_photos")
    val totalPhotos: Int,
    @SerializedName("twitter_username")
    val twitterUsername: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("uploads_remaining")
    val uploadsRemaining: Int,
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
}