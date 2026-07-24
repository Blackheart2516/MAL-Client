package com.nikhil.malclient.model

data class AnimeListResponse(
    val data: List<AnimeListItem>
)

data class AnimeListItem(
    val node: Anime,
    val list_status: ListStatus
)

data class ListStatus(
    val status: String,
    val score: Int,
    val num_episodes_watched: Int
)