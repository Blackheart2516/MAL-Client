package com.nikhil.malclient.cache

import android.content.Context


class AppCache(
    context: Context
) {

    private val prefs =
        context.getSharedPreferences(
            "app_cache",
            Context.MODE_PRIVATE
        )


    fun saveUsername(username: String) {

        prefs.edit()
            .putString("username", username)
            .apply()

    }


    fun getUsername(): String {

        return prefs.getString(
            "username",
            ""
        ) ?: ""

    }


    fun savePicture(picture: String?) {

        prefs.edit()
            .putString("picture", picture)
            .apply()

    }


    fun getPicture(): String? {

        return prefs.getString(
            "picture",
            null
        )

    }


    fun saveEpisode(
        animeId: Int,
        episodes: Int
    ) {

        prefs.edit()
            .putInt(
                "episode_$animeId",
                episodes
            )
            .apply()

    }


    fun getEpisode(
        animeId: Int
    ): Int? {


        val key = "episode_$animeId"


        if (!prefs.contains(key)) {

            return null

        }


        return prefs.getInt(
            key,
            0
        )

    }

}