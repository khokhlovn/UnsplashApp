package com.example.unsplashapp.ui.collections

import com.example.unsplashapp.ui.collections.adapter.CollectionsPagingAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CollectionsModule {

    @Singleton
    @Provides
    fun provideCollectionsPagingAdapter() = CollectionsPagingAdapter()
}
