package com.nikhil.malclient.cache

import android.content.Context
import com.google.gson.Gson
import com.nikhil.malclient.model.AnimeListResponse


class MyListCache(
    context: Context
) {


    private val prefs =
        context.getSharedPreferences(
            "mylist_cache",
            Context.MODE_PRIVATE
        )


    private val gson = Gson()



    fun saveList(
        key: String,
        data: AnimeListResponse
    ) {


        prefs.edit()
            .putString(
                key,
                gson.toJson(data)
            )
            .apply()

    }



    fun getList(
        key: String
    ): AnimeListResponse? {


        val json =
            prefs.getString(
                key,
                null
            )


        return if(json != null) {


            gson.fromJson(
                json,
                AnimeListResponse::class.java
            )


        } else {

            null

        }

    }

}