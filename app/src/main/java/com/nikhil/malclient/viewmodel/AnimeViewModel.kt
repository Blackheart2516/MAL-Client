package com.nikhil.malclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.malclient.model.AnimeResponse
import com.nikhil.malclient.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.nikhil.malclient.model.Anime
import android.util.Log

class AnimeViewModel : ViewModel() {

    private val repository = AnimeRepository()

    private val _anime = MutableStateFlow<AnimeResponse?>(null)
    val anime: StateFlow<AnimeResponse?> = _anime
    private val _selectedAnime = MutableStateFlow<Anime?>(null)
    val selectedAnime: StateFlow<Anime?> = _selectedAnime

    fun searchAnime(clientId: String, query: String) {
        viewModelScope.launch {
            val response = repository.searchAnime(clientId, query)
            if (response.isSuccessful) {
                _anime.value = response.body()
            }
        }
    }
    fun loadAnimeDetails(clientId: String, animeId: String) {
        viewModelScope.launch {
            val response = repository.getAnimeDetails(clientId, animeId)

            if (response.isSuccessful) {
                _selectedAnime.value = response.body()
            } else {
                Log.e(
                    "MAL_API",
                    "Details Error: ${response.code()} ${response.message()}"
                )
            }
        }
    }
}