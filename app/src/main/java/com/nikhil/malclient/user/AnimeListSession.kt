object AnimeListSession {


    private val animeStatusMap =
        mutableMapOf<Int, String>()


    private val watchedEpisodeMap =
        mutableMapOf<Int, Int>()



    fun setAnimeStatus(
        animeId: Int,
        status: String
    ) {

        animeStatusMap[animeId] = status

    }



    fun getAnimeStatus(
        animeId: Int
    ): String? {

        return animeStatusMap[animeId]

    }



    fun isAnimeInList(
        animeId: Int
    ): Boolean {

        return animeStatusMap.containsKey(animeId)

    }



    fun removeAnime(
        animeId: Int
    ) {

        animeStatusMap.remove(animeId)

        watchedEpisodeMap.remove(animeId)

    }




    fun setWatchedEpisodes(
        animeId: Int,
        episodes: Int
    ) {

        watchedEpisodeMap[animeId] = episodes

    }




    fun getWatchedEpisodes(
        animeId: Int
    ): Int {

        return watchedEpisodeMap[animeId] ?: 0

    }

}