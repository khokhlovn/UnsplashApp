package com.example.unsplashapp.api

import com.example.unsplashapp.api.resp.CollectionsResponse
import com.example.unsplashapp.api.resp.CurrentUserResponse
import com.example.unsplashapp.api.resp.PhotosResponse
import com.example.unsplashapp.api.resp.SearchResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.http.*

@ActivityRetainedScoped
interface UnsplashService {

    @GET("photos")
    suspend fun getPhotos(
        @Header("Authorization") authHeader: String,
        @Query("order_by") orderBy: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PhotosResponse

    @GET("search/photos")
    suspend fun searchPhotos(
        @Header("Authorization") authHeader: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchResponse

    @GET("collections")
    suspend fun getCollections(
        @Header("Authorization") authHeader: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): CollectionsResponse

    @GET("collections/{id}/photos")
    suspend fun getCollectionPhotos(
        @Path("id") id: String,
        @Header("Authorization") authHeader: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PhotosResponse

    @POST("photos/{id}/like")
    suspend fun likePhoto(
        @Path("id") id: String,
        @Header("Authorization") authHeader: String
    ): PhotosResponse.PhotosResponseItem

    @DELETE("photos/{id}/like")
    suspend fun unlikePhoto(
        @Path("id") id: String,
        @Header("Authorization") authHeader: String
    ): PhotosResponse.PhotosResponseItem

    @GET("me")
    suspend fun getUserInfo(
        @Header("Authorization") authHeader: String
    ): CurrentUserResponse

    @GET("users/{username}/photos")
    suspend fun getUserPhotos(
        @Path("username") username: String,
        @Header("Authorization") authHeader: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): PhotosResponse
}
