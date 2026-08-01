package com.nikhil.malclient.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.nikhil.malclient.user.AnimeListManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import com.nikhil.malclient.viewmodel.AnimeDetailsViewModel
import com.nikhil.malclient.viewmodel.AniListViewModel
import com.nikhil.malclient.viewmodel.MyListViewModel


@Composable
fun AnimeDetailsScreen(

    animeId: String,

    token: String,

    myListViewModel: MyListViewModel

) {


    Log.d(
        "DETAIL_SCREEN",
        "opened id=$animeId token=${token.isNotEmpty()}"
    )


    val viewModel: AnimeDetailsViewModel =
        viewModel()



    val anime =
        viewModel.anime.collectAsState().value

    val episodeMap =
        AnimeListManager.episodeMap
            .collectAsState()
            .value

    val statusMap =
        AnimeListManager.statusMap
            .collectAsState()
            .value

    val removedAnime =
        AnimeListManager.removedAnime
            .collectAsState()
            .value

    val scope =
        rememberCoroutineScope()

    var statusMenuExpanded by remember {
        mutableStateOf(false)
    }



    val context =
        LocalContext.current





    val aniListViewModel: AniListViewModel =
        viewModel(

            factory = object :
                ViewModelProvider.Factory {


                override fun <T : androidx.lifecycle.ViewModel> create(
                    modelClass: Class<T>
                ): T {


                    return AniListViewModel(
                        context
                    ) as T


                }


            }

        )





    LaunchedEffect(animeId) {


        viewModel.loadAnimeDetails(

            token,

            animeId

        )


    }





    LaunchedEffect(anime?.id) {


        anime?.let {


            aniListViewModel.loadAiredEpisodes(

                it.id

            )


        }


    }





    val airedEpisodes =
        anime?.let {


            aniListViewModel
                .airedEpisodesMap[it.id]


        }






    if (anime == null) {


        Box(

            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Color(0xFF0B1628)
                    ),


            contentAlignment =
                Alignment.Center

        ) {


            CircularProgressIndicator(
                color = Color.White
            )


        }


        return

    }





    Column(

        modifier = Modifier

            .fillMaxSize()

            .background(
                Color(0xFF0B1628)
            )

            .statusBarsPadding()

            .padding(
                top = 8.dp,
                start = 12.dp,
                end = 12.dp,
                bottom = 16.dp
            )

            .verticalScroll(
                rememberScrollState()
            )


    ) {

        Card(

            modifier =
                Modifier.fillMaxWidth(),


            shape =
                RoundedCornerShape(12.dp),


            colors =
                CardDefaults.cardColors(

                    containerColor =
                        Color(0xFF14243A)

                )

        ) {


            Image(

                painter =
                    rememberAsyncImagePainter(

                        anime.main_picture?.large
                            ?: anime.main_picture?.medium

                    ),


                contentDescription =
                    anime.title,


                modifier =
                    Modifier

                        .fillMaxWidth()

                        .height(320.dp),


                contentScale =
                    ContentScale.Fit

            )


        }





        Spacer(
            modifier = Modifier.height(16.dp)
        )





        Text(

            text = anime.title,

            color = Color(0xFF42A5F5),

            fontWeight = FontWeight.Bold,

            style = MaterialTheme.typography.headlineSmall

        )





        Spacer(
            modifier = Modifier.height(10.dp)
        )





        Text(

            text =
                "⭐ Score: ${anime.mean ?: "N/A"}",

            color = Color(0xFFFFD54F)

        )





        Text(

            text =
                "📺 Aired Episodes: ${
                    airedEpisodes
                        ?: anime.num_episodes
                        ?: "?"
                }",

            color = Color(0xFFBDBDBD)

        )





        Text(

            text =
                "📌 Status: ${anime.status ?: "Unknown"}",

            color = Color(0xFF8FA8C8)

        )





        Spacer(
            modifier = Modifier.height(12.dp)
        )





        val myStatus =
            if (removedAnime.contains(anime.id)) {

                null

            }
            else {

                statusMap[anime.id]
                    ?: anime.my_list_status?.status
                    ?: AnimeListSession.getAnimeStatus(anime.id)

            }


        val myWatched =
            episodeMap[anime.id]
                ?: anime.my_list_status?.num_episodes_watched
                ?: AnimeListSession.getWatchedEpisodes(anime.id)

        var watchedEpisodes by remember {
            mutableIntStateOf(myWatched)
        }





        if (myStatus == null) {


            Button(

                onClick = {


                    viewModel.addToList(

                        token = token,

                        animeId = anime.id.toString(),

                        status = "plan_to_watch"

                    ) { success ->



                        if (success) {


                            AnimeListSession.setAnimeStatus(

                                anime.id,

                                "plan_to_watch"

                            )

                            AnimeListManager.updateStatus(

                                animeId = anime.id,

                                status = "plan_to_watch"

                            )



                            AnimeListSession.setWatchedEpisodes(

                                anime.id,

                                0

                            )

                            AnimeListManager.updateEpisodes(

                                animeId = anime.id,

                                episodes = 0

                            )



                            scope.launch {


                                myListViewModel.refreshMyList(

                                    token

                                )


                            }



                            Log.d(

                                "ADD_LIST",

                                "Added to Plan To Watch"

                            )


                        }
                        else {


                            Log.e(

                                "ADD_LIST",

                                "Failed"

                            )


                        }


                    }


                }

            ) {


                Text(

                    text = "➕ Add to List"

                )


            }


        }
        else {


            Box {

                Button(

                    onClick = {

                        statusMenuExpanded = true

                    }

                ) {

                    Text(

                        text =
                            "My List: ${
                                myStatus
                                    .replace("_", " ")
                                    .replaceFirstChar {

                                        it.uppercase()

                                    }
                            }"

                    )

                }



                DropdownMenu(

                    expanded = statusMenuExpanded,

                    onDismissRequest = {

                        statusMenuExpanded = false

                    }

                ) {


                    listOf(

                        "plan_to_watch",

                        "watching",

                        "completed",

                        "on_hold",

                        "dropped"

                    ).forEach { status ->



                        DropdownMenuItem(

                            text = {

                                Text(

                                    status
                                        .replace("_", " ")
                                        .uppercase()

                                )

                            },


                            onClick = {


                                statusMenuExpanded = false



                                AnimeListManager.updateStatus(

                                    animeId = anime.id,

                                    status = status

                                )



                                viewModel.updateAnimeStatus(

                                    token = token,

                                    animeId = anime.id.toString(),

                                    status = status

                                )


                            }

                        )


                    }


                }

            }





            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.Center,

                verticalAlignment = Alignment.CenterVertically

            ) {


                Button(

                    onClick = {


                        watchedEpisodes =
                            (watchedEpisodes - 1)
                                .coerceAtLeast(0)



                        AnimeListManager.updateEpisodes(

                            animeId = anime.id,

                            episodes = watchedEpisodes

                        )



                        val newStatus =
                            AnimeListManager.calculateStatus(

                                watchedEpisodes = watchedEpisodes,

                                totalEpisodes = anime.num_episodes ?: 0

                            )



                        AnimeListManager.updateStatus(

                            animeId = anime.id,

                            status = newStatus

                        )



                        viewModel.updateAnimeStatus(

                            token = token,

                            animeId = anime.id.toString(),

                            status = newStatus

                        )



                        myListViewModel.updateEpisodeProgress(

                            token = token,

                            animeId = anime.id.toString(),

                            episodes = watchedEpisodes

                        )


                    }

                ) {

                    Text("-")

                }



                Spacer(
                    modifier = Modifier.width(12.dp)
                )



                Text(

                    text =
                        "Watched: $watchedEpisodes / ${airedEpisodes ?: "?"}" ,

                    modifier = Modifier,

                    maxLines = 1,



                    color = Color(0xFFBDBDBD)

                )



                Spacer(
                    modifier = Modifier.width(12.dp)
                )



                Button(

                    onClick = {


                        val maxEpisodes =
                            airedEpisodes ?: anime.num_episodes ?: 0



                        watchedEpisodes =
                            if(maxEpisodes > 0) {

                                (watchedEpisodes + 1)
                                    .coerceAtMost(maxEpisodes)

                            }
                            else {

                                watchedEpisodes + 1

                            }



                        AnimeListManager.updateEpisodes(

                            animeId = anime.id,

                            episodes = watchedEpisodes

                        )

                        val newStatus =
                            AnimeListManager.calculateStatus(

                                watchedEpisodes = watchedEpisodes,

                                totalEpisodes = anime.num_episodes ?: 0

                            )


                        AnimeListManager.updateStatus(

                            animeId = anime.id,

                            status = newStatus

                        )

                        viewModel.updateAnimeStatus(

                            token = token,

                            animeId = anime.id.toString(),

                            status = newStatus

                        )



                        myListViewModel.updateEpisodeProgress(

                            token = token,

                            animeId = anime.id.toString(),

                            episodes = watchedEpisodes

                        )


                    }

                ) {

                    Text("+")

                }


            }


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            Button(

                onClick = {


                    viewModel.removeFromList(

                        token = token,

                        animeId = anime.id.toString()

                    ) { success ->


                        if(success) {


                            Log.d(
                                "REMOVE_LIST",
                                "Removed ${anime.title}"
                            )


                        }


                    }


                },

                colors = ButtonDefaults.buttonColors(

                    containerColor = Color.Red

                )

            ) {


                Text(

                    text = "🗑 Remove from List"

                )


            }


        }

        Spacer(

            modifier =
                Modifier.height(14.dp)

        )





        Card(

            modifier =
                Modifier.fillMaxWidth(),


            shape =
                RoundedCornerShape(12.dp),


            colors =
                CardDefaults.cardColors(

                    containerColor =
                        Color(0xFF14243A)

                )

        ) {



            Column(

                modifier =
                    Modifier.padding(12.dp)

            ) {



                Text(

                    text = "Genres",

                    color = Color.White,

                    fontWeight = FontWeight.Bold

                )





                Spacer(

                    modifier =
                        Modifier.height(6.dp)

                )





                Text(

                    text =
                        anime.genres
                            ?.joinToString(", ") {

                                it.name

                            }
                            ?: "N/A",


                    color =
                        Color(0xFFBDBDBD)

                )



            }


        }







        Spacer(

            modifier =
                Modifier.height(12.dp)

        )








        Card(

            modifier =
                Modifier.fillMaxWidth(),


            shape =
                RoundedCornerShape(12.dp),


            colors =
                CardDefaults.cardColors(

                    containerColor =
                        Color(0xFF14243A)

                )

        ) {



            Column(

                modifier =
                    Modifier.padding(12.dp)

            ) {



                Text(

                    text = "Synopsis",

                    color = Color.White,

                    fontWeight = FontWeight.Bold

                )





                Spacer(

                    modifier =
                        Modifier.height(8.dp)

                )





                Text(

                    text =
                        anime.synopsis
                            ?: "No synopsis available",


                    color =
                        Color(0xFFBDBDBD)

                )



            }


        }





    }


}