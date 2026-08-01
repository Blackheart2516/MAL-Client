package com.nikhil.malclient.utils

import android.content.Context


class SearchHistoryManager(
    private val context: Context
) {


    private val prefs =
        context.getSharedPreferences(
            "search_history",
            Context.MODE_PRIVATE
        )


    fun saveSearch(
        query: String
    ) {


        val oldHistory =
            getHistory()
                .toMutableList()



        oldHistory.remove(query)


        oldHistory.add(0, query)



        val finalList =
            oldHistory.take(10)



        prefs.edit()
            .putStringSet(
                "history",
                finalList.toSet()
            )
            .apply()


    }



    fun getHistory(): List<String> {


        return prefs
            .getStringSet(
                "history",
                emptySet()
            )
            ?.toList()
            ?: emptyList()


    }

    fun clearHistory() {

        prefs.edit()
            .remove("history")
            .apply()

    }


}