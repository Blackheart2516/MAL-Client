package com.nikhil.malclient.user

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


object AnimeListManager {


    private val _statusMap =
        MutableStateFlow<Map<Int, String>>(emptyMap())


    val statusMap =
        _statusMap.asStateFlow()



    private val _episodeMap =
        MutableStateFlow<Map<Int, Int>>(emptyMap())


    val episodeMap =
        _episodeMap.asStateFlow()


    private val _removedAnime =
        MutableStateFlow<Set<Int>>(emptySet())


    val removedAnime =
        _removedAnime.asStateFlow()
    private val _statusOverride =
        MutableStateFlow<Map<Int, String>>(emptyMap())


    val statusOverride =
        _statusOverride.asStateFlow()

    fun updateStatus(
        animeId: Int,
        status: String
    ) {
        _removedAnime.value =
            _removedAnime.value - animeId


        _statusMap.value =
            _statusMap.value.toMutableMap()
                .apply {

                    put(
                        animeId,
                        status
                    )

                }
        _statusOverride.value =
            _statusOverride.value.toMutableMap()
                .apply {

                    put(
                        animeId,
                        status
                    )

                }

    }


    fun calculateStatus(
        watchedEpisodes: Int,
        totalEpisodes: Int
    ): String {

        return when {

            watchedEpisodes <= 0 ->
                "plan_to_watch"


            totalEpisodes > 0 &&
                    watchedEpisodes >= totalEpisodes ->
                "completed"


            else ->
                "watching"

        }

    }


    fun updateEpisodes(
        animeId: Int,
        episodes: Int
    ) {


        _episodeMap.value =
            _episodeMap.value.toMutableMap()
                .apply {

                    put(
                        animeId,
                        episodes
                    )

                }


    }





    fun removeAnime(
        animeId: Int
    ) {
        _removedAnime.value =
            _removedAnime.value + animeId


        _statusMap.value =
            _statusMap.value.toMutableMap()
                .apply {

                    remove(animeId)

                }


        _episodeMap.value =
            _episodeMap.value.toMutableMap()
                .apply {

                    remove(animeId)

                }


    }


}