package com.nikhil.malclient.model

data class AnimeResponse(
    val data: List<AnimeData>
)

data class AnimeData(
    val node: Anime
)

data class Anime(
    val id: Int,
    val title: String,
    val main_picture: MainPicture?,
    val mean: Double?,
    val num_episodes: Int?,
    val start_date: String?,
    val end_date: String?,
    val status: String?,
    val synopsis: String?,
    val genres: List<Genre>?
)

data class Genre(
    val name: String
)

data class MainPicture(
    val medium: String?,
    val large: String?
)