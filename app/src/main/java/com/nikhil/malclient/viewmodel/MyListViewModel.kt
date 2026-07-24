package com.nikhil.malclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.malclient.model.AnimeListResponse
import com.nikhil.malclient.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class MyListViewModel : ViewModel() {

    private val repository = AnimeRepository()

    private val _myList =
        MutableStateFlow<AnimeListResponse?>(null)

    val myList: StateFlow<AnimeListResponse?> = _myList


    fun loadMyList(
        token: String,
        status: String? = null
    ) {

        viewModelScope.launch {

            val response = repository.getMyAnimeList(
                token,
                status
            )

            if (response.isSuccessful) {

                response.body()?.data?.forEach {
                    Log.d(
                        "MAL_STATUS",
                        "${it.node.title} -> ${it.list_status.status}"
                    )
                }

                _myList.value = response.body()

            } else {

                Log.e(
                    "MAL_LIST",
                    "Error: ${response.code()}"
                )
            }
        }
    }
}