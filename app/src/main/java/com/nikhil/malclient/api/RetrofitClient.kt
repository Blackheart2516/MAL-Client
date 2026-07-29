package com.nikhil.malclient.api

import com.nikhil.malclient.auth.AuthApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



object RetrofitClient {


    private val okHttpClient = OkHttpClient.Builder()

        .connectTimeout(
            30,
            TimeUnit.SECONDS
        )

        .readTimeout(
            30,
            TimeUnit.SECONDS
        )

        .writeTimeout(
            30,
            TimeUnit.SECONDS
        )

        .build()



    private fun createRetrofit(
        baseUrl: String
    ): Retrofit {

        return Retrofit.Builder()

            .baseUrl(baseUrl)

            .client(okHttpClient)

            .addConverterFactory(
                GsonConverterFactory.create()
            )

            .build()

    }




    val aniListApi: AniListApiService by lazy {


        createRetrofit(
            "https://graphql.anilist.co/"
        )

            .create(
                AniListApiService::class.java
            )

    }




    private val apiRetrofit: Retrofit by lazy {

        createRetrofit(
            ApiConstants.API_BASE_URL
        )

    }



    private val authRetrofit: Retrofit by lazy {

        createRetrofit(
            ApiConstants.AUTH_BASE_URL
        )

    }




    val api: MalApiService by lazy {

        apiRetrofit.create(
            MalApiService::class.java
        )

    }



    val userApi: UserApiService by lazy {

        apiRetrofit.create(
            UserApiService::class.java
        )

    }



    val animeApi: AnimeApiService by lazy {

        apiRetrofit.create(
            AnimeApiService::class.java
        )

    }



    val authApi: AuthApiService by lazy {

        authRetrofit.create(
            AuthApiService::class.java
        )

    }

}