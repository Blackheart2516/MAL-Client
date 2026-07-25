package com.nikhil.malclient.model

data class AnimeListResponse(
    val data: List<AnimeListItem>
)

data class AnimeListItem(
    val node: AnimeListAnime,
    val list_status: ListStatus
)

data class ListStatus(
    val status: String,
    val score: Int,
    val num_episodes_watched: Int
)

data class AnimeListAnime(

    val id: Int,

    val title: String,

    val main_picture: MainPicture?,

    val num_episodes: Int?,

    val start_date: String?,

    val end_date: String?

)