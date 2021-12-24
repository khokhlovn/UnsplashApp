package com.example.unsplashapp.ui.photos

import com.example.unsplashapp.ui.photos.adapter.PhotosPagingAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhotosModule {

    @Singleton
    @Provides
    fun providePhotosPagingAdapter() = PhotosPagingAdapter()
}
