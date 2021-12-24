package com.example.unsplashapp.api

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.unsplashapp.Constants
import com.example.unsplashapp.R
import com.example.unsplashapp.api.resp.CollectionsResponse
import com.example.unsplashapp.api.resp.PhotosResponse
import com.example.unsplashapp.data.CollectionPhotosPagingSource
import com.example.unsplashapp.data.CollectionsPagingSource
import com.example.unsplashapp.data.MyPhotosPagingSource
import com.example.unsplashapp.data.PhotosPagingSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    private val unsplashService: UnsplashService,
    private val photosPagingSource: PhotosPagingSource,
    private val collectionsPagingSource: CollectionsPagingSource,
    private val collectionPhotosPagingSource: CollectionPhotosPagingSource,
    private val myPhotosPagingSource: MyPhotosPagingSource,
    private val sharedPreferences: SharedPreferences,
    private val application: Application
) {

    fun getPhotosFlow(
        query: String = "",
        order: String = "",
        pagingConfig: PagingConfig = getDefaultPageConfig()
    ): Flow<PagingData<PhotosResponse.PhotosResponseItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                photosPagingSource.apply {
                    this.query = query
                    this.order = order
                }
            }
        ).flow
    }

    fun getCollectionPhotosFlow(
        id: String,
        pagingConfig: PagingConfig = getDefaultPageConfig()
    ): Flow<PagingData<PhotosResponse.PhotosResponseItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { collectionPhotosPagingSource.apply { this.collectionId = id } }
        ).flow
    }

    fun getMyPhotosFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig()
    ): Flow<PagingData<PhotosResponse.PhotosResponseItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { myPhotosPagingSource }
        ).flow
    }

    fun getCollectionsFlow(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<CollectionsResponse.CollectionsResponseItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { collectionsPagingSource }
        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = Constants.DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(application.resources.getString(R.string.saved_token_key), token)
            apply()
        }
    }

    fun checkIfTokenExists(): Boolean {
        return sharedPreferences
            .getString(application.resources.getString(R.string.saved_token_key), "") != ""
    }

    private fun getToken(): String {
        return "Bearer " + sharedPreferences
            .getString(application.resources.getString(R.string.saved_token_key), "")
    }

    fun likePhotoFlow(id: String): Flow<PhotosResponse.PhotosResponseItem> = flow {
        val resp = unsplashService.likePhoto(id, getToken())
        emit(resp)
    }

    fun unlikePhotoFlow(id: String): Flow<PhotosResponse.PhotosResponseItem> = flow {
        val resp = unsplashService.unlikePhoto(id, getToken())
        emit(resp)
    }

    fun downloadImage(title: String, url: String) {
        val direct = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .absolutePath + "/" + title + "/"
        )
        if (!direct.exists()) {
            direct.mkdir()
        }
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(title)
            .setMimeType("image/jpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                File.separator + "UnsplashApp" + File.separator + title
            )
        (application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
    }
}
