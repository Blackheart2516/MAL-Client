package com.nikhil.malclient.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.malclient.cache.EpisodeCache
import com.nikhil.malclient.repository.AniListRepository
import kotlinx.coroutines.launch


class AniListViewModel(
    private val context: Context
) : ViewModel() {


    private val repository =
        AniListRepository()


    private val cache =
        EpisodeCache(context)



    // Stores episode count
    // Example:
    // 21 -> 1171
    val airedEpisodesMap =
        mutableStateMapOf<Int, Int?>()



    // true  = AniList responded
    // false = AniList failed
    val aniListStatusMap =
        mutableStateMapOf<Int, Boolean>()



    // Prevent duplicate API calls
    private val loadingAniList =
        mutableSetOf<Int>()



    fun loadAiredEpisodes(
        malId: Int
    ) {


        // Already loaded
        if (
            airedEpisodesMap[malId] != null
        ) {
            return
        }


        // Already requesting
        if (loadingAniList.contains(malId)) {
            return
        }



        loadingAniList.add(malId)



        viewModelScope.launch {


            val result =
                repository.getAiredEpisodes(
                    malId
                )



            Log.d(
                "ANILIST_RESULT",
                "$malId = ${result.episodes} success=${result.success}"
            )



            if (result.success && result.episodes != null) {


                // AniList success
                // Save to memory

                airedEpisodesMap[malId] =
                    result.episodes



                // Save permanently

                cache.saveEpisode(
                    animeId = malId,
                    episodes = result.episodes
                )



                Log.d(
                    "EP_CACHE",
                    "$malId saved=${result.episodes}"
                )


            }
            else if (!result.success) {


                val cachedEpisode =
                    cache.getEpisode(malId)


                if (cachedEpisode != null) {

                    airedEpisodesMap[malId] =
                        cachedEpisode


                    Log.d(
                        "EP_CACHE",
                        "$malId loaded=$cachedEpisode"
                    )

                }

            }
            else {

                // AniList responded but no data
                // Try cache instead of storing null

                val cachedEpisode =
                    cache.getEpisode(
                        malId
                    )


                if (cachedEpisode != null) {

                    airedEpisodesMap[malId] =
                        cachedEpisode


                    Log.d(
                        "EP_CACHE",
                        "$malId fallback=$cachedEpisode"
                    )

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