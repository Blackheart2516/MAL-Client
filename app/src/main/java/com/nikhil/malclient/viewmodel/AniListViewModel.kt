package com.nikhil.malclient.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.malclient.cache.AppCache
import com.nikhil.malclient.repository.AniListRepository
import kotlinx.coroutines.launch


class AniListViewModel(
    private val context: Context
) : ViewModel() {


    private val repository =
        AniListRepository()



    private val cache =
        AppCache(context)




    // MAL ID -> aired episodes
    val airedEpisodesMap =
        mutableStateMapOf<Int, Int?>()



    // MAL ID -> AniList success/failure
    val aniListStatusMap =
        mutableStateMapOf<Int, Boolean>()




    private val loadingAniList =
        mutableSetOf<Int>()




    fun loadAiredEpisodes(
        malId: Int
    ) {


        // 1. Load from memory/cache immediately

        if (airedEpisodesMap[malId] == null) {


            val cachedEpisode =
                cache.getEpisode(malId)



            if (cachedEpisode != null) {


                airedEpisodesMap[malId] =
                    cachedEpisode



                Log.d(
                    "EP_CACHE",
                    "$malId instant=$cachedEpisode"
                )

            }

        }




        // 2. Prevent duplicate API requests

        if (loadingAniList.contains(malId)) {

            return

        }



        loadingAniList.add(malId)





        // 3. Update from AniList in background

        viewModelScope.launch {


            try {



                val result =
                    repository.getAiredEpisodes(
                        malId
                    )




                Log.d(
                    "ANILIST_RESULT",
                    "$malId episodes=${result.episodes} success=${result.success}"
                )





                if (
                    result.success &&
                    result.episodes != null
                ) {



                    // Update UI

                    airedEpisodesMap[malId] =
                        result.episodes





                    // Update cache

                    cache.saveEpisode(

                        animeId = malId,

                        episodes = result.episodes

                    )



                    Log.d(
                        "EP_CACHE",
                        "$malId saved=${result.episodes}"
                    )



                }



                aniListStatusMap[malId] =
                    result.success





            } catch (e: Exception) {



                Log.e(
                    "ANILIST_ERROR",
                    "$malId ${e.message}"
                )



                // Keep existing cache value

                if (
                    airedEpisodesMap[malId] == null
                ) {


                    val fallback =
                        cache.getEpisode(malId)



                    if (fallback != null) {


                        airedEpisodesMap[malId] =
                            fallback



                        Log.d(
                            "EP_CACHE",
                            "$malId fallback=$fallback"
                        )


                    }

                }



                aniListStatusMap[malId] =
                    false


            } finally {



                loadingAniList.remove(malId)


            }



        }



    }


}