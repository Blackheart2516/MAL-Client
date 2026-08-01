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

    val end_date: String?,


    val my_list_status: MyListStatus?

)



data class MyListStatus(

    val status: String?,

    val num_episodes_watched: Int = 0,

    val score: Int = 0

)