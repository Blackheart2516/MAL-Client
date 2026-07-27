package com.nikhil.malclient.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

import com.nikhil.malclient.model.Anime
import com.nikhil.malclient.model.AnimeListResponse


@Composable
fun AnimeCard(
    anime: Anime,
    onClick: () -> Unit,
    userAnimeList: AnimeListResponse?,
    airedEpisode: Int?,
    aniListSuccess: Boolean?
) {


    Log.d(
        "EP_CHECK",
        "${anime.title} total=${anime.num_episodes} aired=$airedEpisode success=$aniListSuccess"
    )


    val myAnime =
        userAnimeList
            ?.data
            ?.find {
                it.node.id == anime.id
            }



    val myStatus =
        myAnime
            ?.list_status
            ?.status



    Card(

        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 4.dp,
                vertical = 4.dp
            )

    ) {


        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        ) {



            Image(

                painter = rememberAsyncImagePainter(
                    anime.main_picture?.medium
                ),

                contentDescription = anime.title,

                modifier = Modifier
                    .width(75.dp)
                    .height(110.dp),

                contentScale = ContentScale.Crop

            )



            Spacer(
                modifier = Modifier.width(12.dp)
            )



            Column(

                modifier = Modifier.weight(1f)

            ) {



                Text(

                    text = anime.title,

                    style = MaterialTheme.typography.titleMedium,

                    fontWeight = FontWeight.Bold,

                    maxLines = 1

                )



                Spacer(
                    modifier = Modifier.height(6.dp)
                )



                Text(
                    text = "⭐ Score: ${anime.mean ?: "N/A"}"
                )



                // Episode logic
                // 1. MAL total episodes
                // 2. AniList currently aired
                // 3. ?

                val displayEpisodes =
                    when {


                        anime.num_episodes != null &&
                                anime.num_episodes > 0 ->

                            anime.num_episodes



                        airedEpisode != null &&
                                airedEpisode > 0 ->

                            airedEpisode



                        else ->

                            "?"

                    }



                Text(

                    text = "📺 Episodes: $displayEpisodes"

                )



                Text(

                    text = "📌 Status: ${
                        anime.status ?: "Unknown"
                    }",

                    maxLines = 1

                )



                Spacer(
                    modifier = Modifier.height(6.dp)
                )



                if(myStatus != null) {



                    Text(

                        text = myStatus
                            .replace("_", " ")
                            .uppercase(),

                        fontWeight = FontWeight.Bold

                    )



                } else {



                    Button(

                        onClick = {

                            // Add to list later

                        },


                        modifier = Modifier.height(30.dp),


                        contentPadding = PaddingValues(

                            horizontal = 12.dp,

                            vertical = 2.dp

                        )

                    ) {



                        Text(

                            text = "Add to List",

                            fontSize = 12.sp

                        )

                    }


                }


            }


        }


    }


}