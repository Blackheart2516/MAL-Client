package com.nikhil.malclient.model


data class AniListResponse(

    val data: AniListData

)


data class AniListData(

    val Media: AniListMedia

)


data class AniListMedia(

    val episodes: Int?,

    val nextAiringEpisode: NextAiringEpisode?

)


data class NextAiringEpisode(

    val episode: Int?

)