package com.nikhil.malclient.model

data class AnimeListResponse(
    val data: List<AnimeListItem>,
    val paging: Paging?
)


data class Paging(
    val next: String?
)

data class AnimeListItem(
    val node: AnimeListAnime,
    val list_status: ListStatus
)

data class ListStatus(
    val status: String,
    val score: Int,
    val num_episodes_watched: Int,
    val updated_at: String?
)

data class AnimeListAnime(

    val id: Int,

    val title: String,

    val main_picture: MainPicture?,

    val num_episodes: Int?,

    val start_date: String?,

    val end_date: String?

)