package com.nikhil.malclient.repository

import android.util.Log
import com.nikhil.malclient.api.RetrofitClient


class AniListRepository {


    data class AniListEpisodeResult(

        val episodes: Int?,

        val success: Boolean

    )



    suspend fun getAiredEpisodes(

        malId: Int

    ): AniListEpisodeResult {



        return try {


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



            val response = try {

                RetrofitClient.aniListApi.getAnimeInfo(
                    body = mapOf(
                        "query" to query
                    )
                )

            } catch (e: Exception) {

                Log.e(
                    "ANILIST_NETWORK_ERROR",
                    e.message ?: "timeout"
                )

                return AniListEpisodeResult(
                    episodes = null,
                    success = false
                )

            }




            val media =

                response.body()

                    ?.data

                    ?.Media




            Log.d(

                "ANILIST_MEDIA_CHECK",

                "malId=$malId media=$media"

            )




            if (media == null) {



                // AniList has no MAL mapping

                AniListEpisodeResult(

                    episodes = null,

                    success = true

                )



            } else {



                val airedEpisodes =

                    media.nextAiringEpisode

                        ?.episode

                        ?.minus(1)

                        ?: media.episodes

                Log.d(
                    "ANILIST_EPISODE_RESULT",
                    "MAL=$malId next=${media.nextAiringEpisode?.episode} episodes=${media.episodes} final=$airedEpisodes"
                )




                AniListEpisodeResult(

                    episodes = airedEpisodes,

                    success = true

                )



            }




        } catch (e: Exception) {



            Log.e(

                "ANILIST_ERROR",

                e.message ?: "Unknown error"

            )



            // AniList server down / network error

            AniListEpisodeResult(

                episodes = null,

                success = false

            )

        }


    }


}