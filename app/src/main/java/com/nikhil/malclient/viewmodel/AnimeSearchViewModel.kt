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

class AnimeSearchViewModel : ViewModel() {

    private val repository = AnimeRepository()

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList

    private val _userAnimeList =
        MutableStateFlow<AnimeListResponse?>(null)

    val userAnimeList: StateFlow<AnimeListResponse?> = _userAnimeList


    fun loadUserAnimeList(
        token: String
    ) {

        viewModelScope.launch {

            val response = repository.getMyAnimeList(
                token = token
            )


            if(response.isSuccessful) {

                _userAnimeList.value = response.body()

            }

        }

    }

    fun search(
        token: String,
        query: String
    ) {

        viewModelScope.launch {

            val response = repository.searchAnime(
                token,
                query.trim()
            )

            if (response.isSuccessful) {

                val result =
                    response.body()?.data?.map {
                        it.node
                    } ?: emptyList()

                _animeList.value = result

                Log.d(
                    "SEARCH_RESULT",
                    result.joinToString(", ") { anime ->
                        anime.title
                    }
                )
            }
        }
    }
}