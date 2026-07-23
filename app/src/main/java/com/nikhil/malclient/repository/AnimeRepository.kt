package com.nikhil.malclient.repository

import com.nikhil.malclient.api.MalApiService
import com.nikhil.malclient.api.RetrofitClient

class AnimeRepository {

    private val api = RetrofitClient.instance.create(MalApiService::class.java)

    suspend fun searchAnime(clientId: String, query: String) =
        api.searchAnime(clientId, query)

    suspend fun getAnimeDetails(clientId: String, animeId: String) =
        api.getAnimeDetails(animeId, clientId)
}