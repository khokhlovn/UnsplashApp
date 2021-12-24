package com.example.unsplashapp.data

import android.app.Application
import android.content.SharedPreferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplashapp.Constants
import com.example.unsplashapp.R
import com.example.unsplashapp.api.UnsplashService
import com.example.unsplashapp.api.resp.PhotosResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class MyPhotosPagingSource @Inject constructor(
    private val unsplashService: UnsplashService,
    private val sharedPreferences: SharedPreferences,
    private val application: Application
) :
    PagingSource<Int, PhotosResponse.PhotosResponseItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotosResponse.PhotosResponseItem> {
        val page = params.key ?: Constants.DEFAULT_PAGE_INDEX
        return try {
            val token = sharedPreferences.getString(
                application.resources.getString(R.string.saved_token_key),
                ""
            )
            val username = unsplashService.getUserInfo("Bearer $token").username
            val response = unsplashService.getUserPhotos(
                username,
                "Bearer $token",
                page,
                Constants.DEFAULT_PAGE_SIZE
            )
            LoadResult.Page(
                response,
                prevKey = if (page == 1) null else page,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PhotosResponse.PhotosResponseItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
