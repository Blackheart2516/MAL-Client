package com.nikhil.malclient.api

import com.nikhil.malclient.user.UserProfile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApiService {

    @GET("users/@me")
    suspend fun getMyProfile(
        @Header("Authorization")
        token: String
    ): Response<UserProfile>
}