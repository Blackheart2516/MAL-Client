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
import retrofit2.http.DELETE
import com.nikhil.malclient.model.AnimeListResponse
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Field
import com.nikhil.malclient.model.AnimeListStatusRequest


interface AnimeApiService {

    @GET("anime")
    suspend fun searchAnime(
        @Header("Authorization") token: String,
        @Query("q") query: String,
        @Query("fields") fields: String =
            "mean,num_episodes,status,main_picture,genres",
        @Query("limit") limit: Int = 50
    ): Response<AnimeResponse>


    @GET("anime/{anime_id}")
    suspend fun getAnimeDetails(
        @Header("Authorization") token: String,
        @Path("anime_id") animeId: String,
        @Query("fields") fields: String =
            "synopsis,mean,num_episodes,status,main_picture,genres,studios,my_list_status"
    ): Response<Anime>

    @FormUrlEncoded
    @PUT("anime/{anime_id}/my_list_status")
    suspend fun addAnimeToList(

        @Header("Authorization") token: String,

        @Path("anime_id") animeId: String,

        @Field("status") status: String

    ): Response<Unit>

    @GET("users/@me/animelist")
    suspend fun getMyAnimeList(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null,
        @Query("fields") fields: String = "list_status,num_episodes,start_date,end_date",
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): Response<AnimeListResponse>

    @FormUrlEncoded
    @PUT("anime/{anime_id}/my_list_status")
    suspend fun updateEpisodeProgress(
        @Header("Authorization") token: String,
        @Path("anime_id") animeId: String,
        @Field("num_watched_episodes") episodes: Int
    ): Response<Unit>

    @DELETE("anime/{anime_id}/my_list_status")
    suspend fun removeAnimeFromList(

        @Header("Authorization") token: String,

        @Path("anime_id") animeId: String

    ): Response<Unit>
}


