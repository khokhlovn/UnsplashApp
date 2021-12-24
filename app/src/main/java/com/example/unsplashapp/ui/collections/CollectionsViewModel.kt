package com.example.unsplashapp.ui.collections

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplashapp.api.Repository
import com.example.unsplashapp.api.resp.CollectionsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    fun fetchCollections(): Flow<PagingData<CollectionsResponse.CollectionsResponseItem>> {
        return repository.getCollectionsFlow()
            .cachedIn(viewModelScope)
    }
}
