package com.example.unsplashapp.ui.photos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplashapp.api.Repository
import com.example.unsplashapp.api.resp.PhotosResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    var order = "Latest"

    fun fetchPhotos(query: String = ""): Flow<PagingData<PhotosResponse.PhotosResponseItem>> {
        return repository.getPhotosFlow(query, order.lowercase())
            .cachedIn(viewModelScope)
    }
}
