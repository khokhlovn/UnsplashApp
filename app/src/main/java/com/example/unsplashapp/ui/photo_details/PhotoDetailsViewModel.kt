package com.example.unsplashapp.ui.photo_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.unsplashapp.api.Repository
import com.example.unsplashapp.api.resp.PhotosResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    fun likePhoto(id: String): Flow<PhotosResponse.PhotosResponseItem> {
        return repository.likePhotoFlow(id)
    }

    fun unlikePhoto(id: String): Flow<PhotosResponse.PhotosResponseItem> {
        return repository.unlikePhotoFlow(id)
    }

    fun download(id: String, url: String) {
        repository.downloadImage("$id.jpeg", url)
    }
}
