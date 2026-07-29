package com.nikhil.malclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.malclient.model.Anime
import com.nikhil.malclient.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.nikhil.malclient.model.AnimeListResponse
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
class AnimeSearchViewModel : ViewModel() {

    private val repository = AnimeRepository()

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList



    private val _userAnimeList =
        MutableStateFlow<AnimeListResponse?>(null)

    val userAnimeList: StateFlow<AnimeListResponse?> = _userAnimeList

    val airedEpisodesMap = mutableStateMapOf<Int, Int>()


    fun loadUserAnimeList(
        token: String
    ) {

        viewModelScope.launch {

            try {


                val response =
                    repository.getMyAnimeList(
                        token = token
                    )



                if (response.isSuccessful) {


                    _userAnimeList.value =
                        response.body()


                }
                else {


                    Log.e(
                        "MYLIST_ERROR",
                        "API failed ${response.code()}"
                    )

                }


            }
            catch (e: Exception) {


                Log.e(
                    "MYLIST_ERROR",
                    "Loading my list failed",
                    e
                )


            }

        }

    }

    fun search(
        token: String,
        query: String
    ) {

        viewModelScope.launch {

            try {


                val response = repository.searchAnime(
                    token,
                    query.trim()
                )


                if (response.isSuccessful) {


                    val result =
                        response.body()
                            ?.data
                            ?.map {
                                it.node
                            }
                            ?: emptyList()



                    _animeList.value =
                        result



                    Log.d(
                        "SEARCH_RESULT",
                        result.joinToString(", ") { anime ->
                            anime.title
                        }
                    )


                }
                else {


                    Log.e(
                        "SEARCH_ERROR",
                        "API failed ${response.code()}"
                    )

                }


            }
            catch (e: Exception) {


                Log.e(
                    "SEARCH_ERROR",
                    "Search failed",
                    e
                )


                _animeList.value =
                    emptyList()

            }

        }

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

                Log.d(
                    "ANILIST_DEBUG",
                    "id=$animeId episodes=${media?.episodes} next=${media?.nextAiringEpisode?.episode}"
                )


                if (media != null) {


                    val airedEpisodes =

                        if (media.nextAiringEpisode?.episode != null) {

                            media.nextAiringEpisode.episode!! - 1

                        } else {

                            media.episodes ?: 0

                        }


                    airedEpisodesMap[animeId] =
                        airedEpisodes.coerceAtLeast(0)


                    Log.d(
                        "SEARCH_AIR_EP",
                        "$animeId = ${airedEpisodesMap[animeId]}"
                    )

                }

            }

        }

    }
}