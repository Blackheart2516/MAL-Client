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


    private val cache = AppCache(context)



    val airedEpisodesMap =
        mutableStateMapOf<Int, Int?>()



    val aniListStatusMap =
        mutableStateMapOf<Int, Boolean>()



    private val loadingAniList =
        mutableSetOf<Int>()





    fun loadAiredEpisodes(
        malId: Int
    ) {


        // Already loaded in memory
        if (airedEpisodesMap[malId] != null) {

            return

        }



        // Load cache first (instant)

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




        // Prevent duplicate API calls

        if (loadingAniList.contains(malId)) {

            return

        }



        loadingAniList.add(malId)




        // Update from AniList in background

        viewModelScope.launch {


            val result =

                repository.getAiredEpisodes(
                    malId
                )




            Log.d(
                "ANILIST_RESULT",
                "$malId = ${result.episodes} success=${result.success}"
            )




            if (
                result.success &&
                result.episodes != null
            ) {



                // Update memory

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




            else {


                // AniList failed
                // Keep cache value if already loaded


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


            }




            aniListStatusMap[malId] =
                result.success





            Log.d(
                "ANILIST_MAP",
                "$malId stored=${airedEpisodesMap[malId]}"
            )




            loadingAniList.remove(malId)



        }


    }


}