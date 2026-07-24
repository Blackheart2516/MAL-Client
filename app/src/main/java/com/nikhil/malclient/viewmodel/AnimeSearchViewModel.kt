package com.nikhil.malclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.malclient.model.Anime
import com.nikhil.malclient.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimeSearchViewModel : ViewModel() {

    private val repository = AnimeRepository()

    private val _animeList = MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList

    fun search(
        token: String,
        query: String
    ) {

        viewModelScope.launch {

            val response = repository.searchAnime(
                token,
                query
            )

            if (response.isSuccessful) {

                val result =
                    response.body()?.data?.map {
                        it.node
                    } ?: emptyList()

                _animeList.value = result
            }
        }
    }
}