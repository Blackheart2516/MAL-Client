package com.nikhil.malclient.repository

import com.nikhil.malclient.api.RetrofitClient
import com.nikhil.malclient.model.AnimeResponse
import retrofit2.Response
import com.nikhil.malclient.model.AnimeListResponse

class AnimeRepository {

    suspend fun searchAnime(
        token: String,
        query: String
    ): Response<AnimeResponse> {

        return RetrofitClient.animeApi.searchAnime(
            token = "Bearer $token",
            query = query
        )
    }


    suspend fun getAnimeDetails(
        token: String,
        animeId: String
    ) = RetrofitClient.animeApi.getAnimeDetails(
        token = "Bearer $token",
        animeId = animeId
    )

    suspend fun addAnimeToList(
        token: String,
        animeId: String,
        status: String
    ): Response<Unit> {

        return RetrofitClient.animeApi.addAnimeToList(
            token = "Bearer $token",
            animeId = animeId,
            status = mapOf(
                "status" to status
            )
        )
    }


    suspend fun getMyAnimeList(
        token: String,
        status: String? = null
    ): Response<AnimeListResponse> {

        return RetrofitClient.animeApi.getMyAnimeList(
            token = "Bearer $token",
            status = status
        )
    }
}