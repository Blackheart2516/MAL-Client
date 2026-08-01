package com.nikhil.malclient.repository

import com.nikhil.malclient.api.RetrofitClient
import com.nikhil.malclient.model.Anime
import com.nikhil.malclient.model.AnimeResponse
import com.nikhil.malclient.model.AnimeListResponse
import com.nikhil.malclient.model.AniListResponse
import retrofit2.Response
import com.nikhil.malclient.model.AnimeListStatusRequest
import android.util.Log

class AnimeRepository {


    suspend fun searchAnime(
        token: String,
        query: String
    ): Response<AnimeResponse> {


        return try {


            RetrofitClient.animeApi.searchAnime(

                token = "Bearer $token",

                query = query,

                limit = 20

            )


        } catch (e: Exception) {


            e.printStackTrace()


            Response.success(

                AnimeResponse(
                    data = emptyList()
                )

            )


        }


    }







    suspend fun getAnimeDetails(
        token: String,
        animeId: String
    ): Response<Anime> {


        return try {


            RetrofitClient.animeApi.getAnimeDetails(

                token = "Bearer $token",

                animeId = animeId

            )


        } catch (e: Exception) {


            e.printStackTrace()


            Response.success(

                Anime(

                    id = animeId.toInt(),

                    title = "Unable to load",

                    main_picture = null,

                    mean = null,

                    num_episodes = null,

                    start_date = null,

                    end_date = null,

                    status = null,

                    synopsis = "Network error",

                    genres = null

                )

            )


        }


    }








    suspend fun addAnimeToList(
        token: String,
        animeId: String,
        status: String
    ): Response<Unit> {
        Log.d(
            "ADD_REQUEST",
            "animeId=$animeId status=$status"
        )


        return try {


            RetrofitClient.animeApi.addAnimeToList(

                token = "Bearer $token",

                animeId = animeId,

                status = status

            )


        } catch (e: Exception) {


            e.printStackTrace()


            Response.success(Unit)


        }


    }









    suspend fun getMyAnimeList(
        token: String,
        status: String? = null,
        limit: Int = 100
    ):Response<AnimeListResponse> {


        return try {


            RetrofitClient.animeApi.getMyAnimeList(

                token = "Bearer $token",

                status = status,

                limit = limit,

                offset = 0

            )


        } catch (e: Exception) {


            e.printStackTrace()


            Response.success(

                AnimeListResponse(

                    data = emptyList(),

                    paging = null

                )

            )


        }


    }









    suspend fun updateEpisodeProgress(
        token: String,
        animeId: String,
        episodes: Int
    ): Response<Unit> {


        return try {


            RetrofitClient.animeApi.updateEpisodeProgress(

                token = "Bearer $token",

                animeId = animeId,

                episodes = episodes

            )


        } catch (e: Exception) {


            e.printStackTrace()


            Response.success(Unit)


        }


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

    suspend fun removeAnimeFromList(
        token: String,
        animeId: String
    ): Response<Unit> {


        return try {


            RetrofitClient.animeApi.removeAnimeFromList(

                token = "Bearer $token",

                animeId = animeId

            )


        }
        catch(e: Exception) {


            e.printStackTrace()


            Response.success(Unit)


        }


    }


}