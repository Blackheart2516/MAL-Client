package com.nikhil.malclient.api

import com.nikhil.malclient.model.AnimeResponse
import com.nikhil.malclient.model.Anime
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.PUT
import com.nikhil.malclient.model.AnimeListResponse


interface AnimeApiService {

    @GET("anime")
    suspend fun searchAnime(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): Response<AnimeResponse>


    @GET("anime/{anime_id}")
    suspend fun getAnimeDetails(
        @Header("Authorization") token: String,
        @Path("anime_id") animeId: String,
        @Query("fields") fields: String =
            "synopsis,mean,num_episodes,status,main_picture,genres,studios"
    ): Response<Anime>

    @PUT("anime/{anime_id}/my_list_status")
    suspend fun addAnimeToList(
        @Header("Authorization") token: String,
        @Path("anime_id") animeId: String,
        @Body status: Map<String, String>
    ): Response<Unit>

    @GET("users/@me/animelist")
    suspend fun getMyAnimeList(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null,
        @Query("fields") fields: String = "list_status",
        @Query("limit") limit: Int = 100
    ): Response<AnimeListResponse>
}
