package com.nikhil.malclient.api

import com.nikhil.malclient.auth.AuthApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val aniListApi: AniListApiService by lazy {

        Retrofit.Builder()

            .baseUrl(
                "https://graphql.anilist.co/"
            )

            .addConverterFactory(
                GsonConverterFactory.create()
            )

            .build()

            .create(
                AniListApiService::class.java
            )

    }

    private val apiRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConstants.AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: MalApiService by lazy {
        apiRetrofit.create(MalApiService::class.java)
    }

    val userApi: UserApiService by lazy {
        apiRetrofit.create(UserApiService::class.java)
    }

    val animeApi: AnimeApiService by lazy {
        apiRetrofit.create(AnimeApiService::class.java)
    }

    val authApi: AuthApiService by lazy {
        authRetrofit.create(AuthApiService::class.java)
    }
}