package com.nikhil.malclient.user

import com.nikhil.malclient.api.RetrofitClient
import retrofit2.Response

class UserRepository {

    private val api = RetrofitClient.userApi

    suspend fun getMyProfile(token: String): Response<UserProfile> {
        return api.getMyProfile("Bearer $token")
    }
}