package com.nikhil.malclient.user

import android.util.Log
import com.nikhil.malclient.api.RetrofitClient
import retrofit2.Response

class UserRepository {

    private val api = RetrofitClient.userApi


    suspend fun getMyProfile(
        token: String
    ): Response<UserProfile>? {


        return try {


            api.getMyProfile(
                "Bearer $token"
            )


        } catch (e: Exception) {


            Log.e(
                "USER_PROFILE_ERROR",
                e.message ?: "Unknown error"
            )


            null

        }

    }

}