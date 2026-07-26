package com.nikhil.malclient.ui.components


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
import android.util.Log


@Composable
fun AnimeCard(
    anime: Anime,
    onClick: () -> Unit,
    userAnimeList: AnimeListResponse?
) {


    val myAnime = userAnimeList
        ?.data
        ?.find {

            it.node.id == anime.id

        }


    val myStatus = myAnime
        ?.list_status
        ?.status
    Log.d(
        "ID_CHECK",
        "${anime.title} searchID=${anime.id} status=$myStatus"
    )
    Log.d(
        "MY_LIST_CHECK",
        userAnimeList?.data
            ?.filter {
                it.node.id == 38826
            }
            ?.joinToString {
                "${it.node.title}=${it.node.id} status=${it.list_status.status}"
            } ?: "NOT FOUND"
    )


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

                modifier = Modifier

                    .weight(1f)

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



                Text(

                    text = "📺 Episodes: ${
                        anime.num_episodes ?: "Unknown"
                    }"

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



                if (myStatus != null) {


                    Text(

                        text = myStatus

                            .replace("_", " ")

                            .uppercase(),


                        fontWeight = FontWeight.Bold

                    )


                } else {



                    Button(

                        onClick = {

                            // Add to List later

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