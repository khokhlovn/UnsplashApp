package com.example.unsplashapp

class Constants {
    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val SCOPE =
            "public read_user write_photos write_likes read_photos read_collections write_collections"
        const val AUTH_ENDPOINT = "https://unsplash.com/oauth/authorize"
        const val TOKEN_ENDPOINT = "https://unsplash.com/oauth/token"
        const val REDIRECT_URI = "https://com.example.unsplashapp"
        const val CLIENT_ID = "1aKf0Z8RGvIywo4QJxrIMA5_iRNI8Lf1X63J5Qkj2r8"
        const val CLIENT_SECRET = "fzPLLjMADaSsr4NuqcvMJeFFZb-O5xS4T_OKHezTtM0"
        const val DEFAULT_PAGE_SIZE = 21
        const val DEFAULT_PAGE_INDEX = 1
        const val SHARED_PREF_NAME = "secret_shared_prefs"
        const val UPLOAD_IMAGE_URL = "https://unsplash.com/?modal=%7B%22tag%22%3A%22Uploader%22%7D"

        enum class Order {
            Latest, Oldest, Popular
        }
    }
}
