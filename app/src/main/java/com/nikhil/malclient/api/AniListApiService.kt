package com.nikhil.malclient.api

import com.nikhil.malclient.model.AniListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface AniListApiService {

    @Headers(
        "Content-Type: application/json"
    )
    @POST("graphql")
    suspend fun getAnimeInfo(
        @Body body: Map<String, String>
    ): Response<AniListResponse>

}