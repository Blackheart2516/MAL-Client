package com.nikhil.malclient.cache

import android.content.Context


class EpisodeCache(
    context: Context
) {


    private val prefs =
        context.getSharedPreferences(
            "episode_cache",
            Context.MODE_PRIVATE
        )



    fun saveEpisode(
        animeId: Int,
        episodes: Int
    ) {

        prefs.edit()
            .putInt(
                animeId.toString(),
                episodes
            )
            .apply()

    }



    fun getEpisode(
        animeId: Int
    ): Int? {


        if (!prefs.contains(animeId.toString())) {

            return null

        }


        return prefs.getInt(
            animeId.toString(),
            0
        )

    }

}