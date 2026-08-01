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
import com.nikhil.malclient.user.AnimeListManager
class AnimeSearchViewModel : ViewModel() {

    private val repository = AnimeRepository()

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList



    private val _userAnimeList =
        MutableStateFlow<AnimeListResponse?>(null)

    val userAnimeList: StateFlow<AnimeListResponse?> = _userAnimeList

    val airedEpisodesMap = mutableStateMapOf<Int, Int>()

    fun updateLocalMyList(
        anime: Anime,
        status: String
    ) {

        val currentList =
            _userAnimeList.value
                ?: return


        val exists =
            currentList.data.any {

                it.node.id == anime.id

            }


        if (!exists) {


            val updatedItem =
                com.nikhil.malclient.model.AnimeListItem(

                    node =
                        com.nikhil.malclient.model.AnimeListAnime(

                            id = anime.id,

                            title = anime.title,

                            main_picture = anime.main_picture,

                            num_episodes = anime.num_episodes,

                            start_date = anime.start_date,

                            end_date = anime.end_date

                        ),


                    list_status =
                        com.nikhil.malclient.model.ListStatus(

                            status = status,

                            score = 0,

                            num_episodes_watched = 0,

                            updated_at = null

                        )

                )



            _userAnimeList.value =
                currentList.copy(

                    data =
                        currentList.data + updatedItem

                )


        }

    }

    fun addToList(
        token: String,
        animeId: String,
        status: String,
        onResult: (Boolean) -> Unit
    ) {

        viewModelScope.launch {

            try {


                val response =
                    repository.addAnimeToList(
                        token = token,
                        animeId = animeId,
                        status = status
                    )


                Log.d(
                    "SEARCH_ADD_LIST",
                    "success=${response.isSuccessful}"
                )


                if(response.isSuccessful) {


                    AnimeListManager.updateStatus(

                        animeId = animeId.toInt(),

                        status = status

                    )


                }


                onResult(
                    response.isSuccessful
                )


            }
            catch (e: Exception) {


                Log.e(
                    "SEARCH_ADD_LIST",
                    e.message ?: "error"
                )


                onResult(false)

            }

        }

    }
    fun loadUserAnimeList(
        token: String
    ) {

        viewModelScope.launch {

            try {


                val response =
                    repository.getMyAnimeList(
                        token = token,
                        limit = 1000
                    )



                if (response.isSuccessful) {




                    Log.d(
                        "SEARCH_MYLIST",
                        "size=${response.body()?.data?.size}"
                    )

                    response.body()?.data?.forEach {

                        Log.d(
                            "SEARCH_MYLIST_ITEM",
                            "${it.node.title} id=${it.node.id} status=${it.list_status.status}"
                        )

                    }
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

    fun refreshUserAnimeList(
        token: String
    ) {
        loadUserAnimeList(token)
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