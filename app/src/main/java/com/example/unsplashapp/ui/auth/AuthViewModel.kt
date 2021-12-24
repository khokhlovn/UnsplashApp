package com.example.unsplashapp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.unsplashapp.api.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    fun checkToken() = repository.checkIfTokenExists()
    fun saveToken(token: String) = repository.saveToken(token)
}
