package com.nikhil.malclient.api

import com.nikhil.malclient.model.Anime
import com.nikhil.malclient.model.AnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MalApiService {

    @GET("anime")
    suspend fun searchAnime(
        @Header("X-MAL-CLIENT-ID") clientId: String,
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("fields") fields: String = "main_picture,mean,num_episodes,status"
    ): Response<AnimeResponse>

    @GET("anime/{id}")
    suspend fun getAnimeDetails(
        @Path("id") id: String,
        @Header("X-MAL-CLIENT-ID") clientId: String,
        @Query("fields")
        fields: String =
            "id,title,main_picture,mean,num_episodes,status,synopsis"
    ): Response<Anime>
}