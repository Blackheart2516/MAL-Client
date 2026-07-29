package com.nikhil.malclient.model


data class AnimeDetails(

    val id: Int,

    val title: String,

    val main_picture: MainPicture?,

    val mean: Double?,

    val num_episodes: Int?,

    val status: String?,

    val synopsis: String?,

    val start_date: String?,

    val end_date: String?

)