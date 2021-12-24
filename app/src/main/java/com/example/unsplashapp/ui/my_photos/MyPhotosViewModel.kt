package com.example.unsplashapp.ui.my_photos

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
class MyPhotosViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    fun fetchMyPhotos(): Flow<PagingData<PhotosResponse.PhotosResponseItem>> {
        return repository.getMyPhotosFlow()
            .cachedIn(viewModelScope)
    }
}
