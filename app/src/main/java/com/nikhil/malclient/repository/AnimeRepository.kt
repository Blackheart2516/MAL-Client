package com.nikhil.malclient.repository

import com.nikhil.malclient.api.RetrofitClient
import com.nikhil.malclient.model.AnimeResponse
import com.nikhil.malclient.model.AnimeListResponse
import com.nikhil.malclient.model.AnimeListItem
import com.nikhil.malclient.model.AniListResponse
import retrofit2.Response


class AnimeRepository {


    suspend fun searchAnime(
        token: String,
        query: String
    ): Response<AnimeResponse> {


        return RetrofitClient.animeApi.searchAnime(
            token = "Bearer $token",
            query = query,
            limit = 50
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


        val allAnime = mutableListOf<AnimeListItem>()


        var offset = 0


        var response: Response<AnimeListResponse>



        do {


            response = RetrofitClient.animeApi.getMyAnimeList(

                token = "Bearer $token",

                status = status,

                limit = 100,

                offset = offset

            )



            if (response.isSuccessful) {


                response.body()?.data?.let {

                    allAnime.addAll(it)

                }

            }



            offset += 100



        } while (

            response.body()?.paging?.next != null

        )



        return Response.success(

            AnimeListResponse(

                data = allAnime,

                paging = null

            )

        )

    }






    suspend fun updateEpisodeProgress(
        token: String,
        animeId: String,
        episodes: Int
    ): Response<Unit> {


        return RetrofitClient.animeApi.updateEpisodeProgress(

            token = "Bearer $token",

            animeId = animeId,

            episodes = episodes

        )

    }






    suspend fun getAniListEpisodes(
        malId: Int
    ): Response<AniListResponse> {


        val query = """
            query {
                Media(idMal: $malId, type: ANIME) {
                    episodes
                    nextAiringEpisode {
                        episode
                    }
                }
            }
        """.trimIndent()



        return RetrofitClient.aniListApi.getAnimeInfo(

            body = mapOf(

                "query" to query

            )

        )

    }


}