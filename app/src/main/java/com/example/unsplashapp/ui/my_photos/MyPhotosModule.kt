package com.example.unsplashapp.ui.my_photos

import com.example.unsplashapp.ui.photos.adapter.PhotosPagingAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyPhotosModule {

    @Singleton
    @Provides
    @MyPhotosQualifier
    fun providePhotosPagingAdapter() = PhotosPagingAdapter()
}
