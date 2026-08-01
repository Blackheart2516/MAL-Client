package com.nikhil.malclient.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.malclient.cache.MyListCache
import com.nikhil.malclient.model.AnimeListResponse
import com.nikhil.malclient.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MyListViewModel(
    private val context: Context
) : ViewModel() {


    private val repository =
        AnimeRepository()


    private val cache =
        MyListCache(context)



    val episodeUpdates =
        mutableStateMapOf<Int, Int>()


    val airedEpisodesMap =
        mutableStateMapOf<Int, Int>()





    private val _allList =
        MutableStateFlow<AnimeListResponse?>(null)

    val allList: StateFlow<AnimeListResponse?> =
        _allList





    private val _watchingList =
        MutableStateFlow<AnimeListResponse?>(null)

    val watchingList: StateFlow<AnimeListResponse?> =
        _watchingList





    private val _completedList =
        MutableStateFlow<AnimeListResponse?>(null)

    val completedList: StateFlow<AnimeListResponse?> =
        _completedList





    private val _planList =
        MutableStateFlow<AnimeListResponse?>(null)

    val planList: StateFlow<AnimeListResponse?> =
        _planList





    private val _onHoldList =
        MutableStateFlow<AnimeListResponse?>(null)

    val onHoldList: StateFlow<AnimeListResponse?> =
        _onHoldList





    private val _droppedList =
        MutableStateFlow<AnimeListResponse?>(null)

    val droppedList: StateFlow<AnimeListResponse?> =
        _droppedList







    fun updateLocalEpisode(
        animeId: Int,
        episodes: Int
    ) {

        episodeUpdates[animeId] =
            episodes

    }







    suspend fun refreshMyList(
        token: String
    ) {

        episodeUpdates.clear()


        loadMyList(
            token = token,
            status = null,
            forceRefresh = true
        )


        loadMyList(
            token = token,
            status = "watching",
            forceRefresh = true
        )


        loadMyList(
            token = token,
            status = "completed",
            forceRefresh = true
        )


        loadMyList(
            token = token,
            status = "plan_to_watch",
            forceRefresh = true
        )


        loadMyList(
            token = token,
            status = "on_hold",
            forceRefresh = true
        )


        loadMyList(
            token = token,
            status = "dropped",
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


            val response =
                repository.getAniListEpisodes(
                    animeId
                )



            if (response.isSuccessful) {


                val media =
                    response.body()
                        ?.data
                        ?.Media



                if (media != null) {


                    val airedEpisodes =

                        if (
                            media.nextAiringEpisode?.episode != null
                        ) {

                            media.nextAiringEpisode.episode!! - 1

                        }
                        else {

                            media.episodes ?: 0

                        }



                    airedEpisodesMap[animeId] =
                        airedEpisodes.coerceAtLeast(0)



                    Log.d(
                        "AIR_EP_CHECK",
                        "$animeId = ${airedEpisodesMap[animeId]}"
                    )

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


            val cacheKey =
                status ?: "all"



            val cachedList =
                cache.getList(
                    cacheKey
                )



            if (
                cachedList != null &&
                !forceRefresh
            ) {


                updateListState(
                    status,
                    cachedList
                )


                Log.d(
                    "MYLIST_CACHE",
                    "Loaded cache $cacheKey size=${cachedList.data.size}"
                )

            }






            val response =
                repository.getMyAnimeList(
                    token,
                    status
                )




            if(response.isSuccessful) {


                val data =
                    response.body()
                data?.data
                    ?.filter {
                        it.node.id == 16870
                    }
                    ?.forEach {

                        Log.d(
                            "CHECK_NARUTO_STATUS",
                            "${it.node.title} => ${it.list_status.status}"
                        )

                    }


                if(data != null) {


                    cache.saveList(

                        key = cacheKey,

                        data = data

                    )



                    val sortedData =
                        data.copy(

                            data =
                                data.data.sortedByDescending {

                                    it.list_status.updated_at ?: ""

                                }

                        )



                    updateListState(
                        status,
                        sortedData
                    )


                }


            }
            else {


                Log.e(
                    "MAL_LIST",
                    "Error ${response.code()}"
                )


            }


        }


    }








    private fun updateListState(
        status: String?,
        data: AnimeListResponse
    ) {


        when(status) {


            null ->
                _allList.value = data



            "watching" ->
                _watchingList.value = data



            "completed" ->
                _completedList.value = data



            "plan_to_watch" ->
                _planList.value = data



            "on_hold" ->
                _onHoldList.value = data



            "dropped" ->
                _droppedList.value = data


        }


    }









    fun updateEpisodeProgress(

        token: String,

        animeId: String,

        episodes: Int

    ) {


        viewModelScope.launch {


            val response =
                repository.updateEpisodeProgress(

                    token,

                    animeId,

                    episodes

                )



            if(response.isSuccessful) {


                Log.d(
                    "MAL_UPDATE",
                    "Episode updated successfully"
                )


            }
            else {


                Log.e(
                    "MAL_UPDATE",
                    "Failed ${response.code()}"
                )


            }


        }


    }


}