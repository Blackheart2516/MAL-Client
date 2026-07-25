package com.nikhil.malclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.malclient.model.AnimeListResponse
import com.nikhil.malclient.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateMapOf


class MyListViewModel : ViewModel() {


    private val repository = AnimeRepository()


    val episodeUpdates = mutableStateMapOf<Int, Int>()

    val airedEpisodesMap = mutableStateMapOf<Int, Int>()
    private val _allList =
        MutableStateFlow<AnimeListResponse?>(null)

    val allList: StateFlow<AnimeListResponse?> = _allList



    private val _watchingList =
        MutableStateFlow<AnimeListResponse?>(null)

    val watchingList: StateFlow<AnimeListResponse?> = _watchingList



    private val _completedList =
        MutableStateFlow<AnimeListResponse?>(null)

    val completedList: StateFlow<AnimeListResponse?> = _completedList



    private val _planList =
        MutableStateFlow<AnimeListResponse?>(null)

    val planList: StateFlow<AnimeListResponse?> = _planList

    fun updateLocalEpisode(
        animeId: Int,
        episodes: Int
    ) {
        episodeUpdates[animeId] = episodes
    }


    suspend fun refreshMyList(
        token: String,
        status: String? = null
    ) {

        episodeUpdates.clear()


        loadMyList(
            token = token,
            status = status,
            forceRefresh = true
        )

    }



    fun loadAiredEpisodes(
        animeId: Int
    ) {

        if (airedEpisodesMap.containsKey(animeId)) {
            return
        }


        viewModelScope.launch {

            val response = repository.getAniListEpisodes(
                animeId
            )


            if (response.isSuccessful) {

                val media =
                    response.body()
                        ?.data
                        ?.Media


                if (media != null) {


                    val airedEpisodes =

                        if (media.nextAiringEpisode?.episode != null) {


                            media.nextAiringEpisode.episode!! - 1


                        } else {


                            media.episodes ?: 0


                        }



                    airedEpisodesMap[animeId] =
                        airedEpisodes.coerceAtLeast(0)



                }

            }

        }

    }


    fun loadMyList(
        token: String,
        status: String? = null,
        forceRefresh: Boolean = false
    ) {


        viewModelScope.launch {


            val response = repository.getMyAnimeList(

                token,

                status

            )


            if(response.isSuccessful) {


                val data = response.body()


                data?.data?.forEach {

                    Log.d(
                        "MAL_STATUS",
                        "${it.node.title} -> ${it.list_status.status}"
                    )

                }



                when(status) {


                    null -> {

                        _allList.value = data

                    }


                    "watching" -> {

                        _watchingList.value = data

                    }


                    "completed" -> {

                        _completedList.value = data

                    }


                    "plan_to_watch" -> {

                        _planList.value = data

                    }

                }






            } else {


                Log.e(

                    "MAL_LIST",

                    "Error: ${response.code()}"

                )

            }

        }

    }





    fun updateEpisodeProgress(

        token: String,

        animeId: String,

        episodes: Int

    ) {


        viewModelScope.launch {


            val response = repository.updateEpisodeProgress(

                token,

                animeId,

                episodes

            )



            if(response.isSuccessful) {


                Log.d(

                    "MAL_UPDATE",

                    "Episode updated successfully"

                )


            } else {


                Log.e(

                    "MAL_UPDATE",

                    "Failed: ${response.code()}"

                )

            }


        }

    }


}