package com.example.unsplashapp.ui.auth

import android.content.Context
import android.net.Uri
import com.example.unsplashapp.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuthService(@ApplicationContext context: Context) = AuthorizationService(context)

    @Singleton
    @Provides
    fun provideServiceConfig() = AuthorizationServiceConfiguration(
        Uri.parse(Constants.AUTH_ENDPOINT),
        Uri.parse(Constants.TOKEN_ENDPOINT)
    )
}
