package com.nikhil.malclient.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.viewmodel.compose.viewModel

import com.nikhil.malclient.ui.components.AnimeCard
import com.nikhil.malclient.viewmodel.AniListViewModel
import com.nikhil.malclient.viewmodel.AnimeSearchViewModel
import com.nikhil.malclient.viewmodel.MyListViewModel


@Composable
fun SearchScreen(
    token: String,
    navController: androidx.navigation.NavController,
    myListViewModel: MyListViewModel
) {


    val context = LocalContext.current


    val viewModel: AnimeSearchViewModel = viewModel()



    val aniListViewModel: AniListViewModel = viewModel(

        factory = object : ViewModelProvider.Factory {

            override fun <T : androidx.lifecycle.ViewModel> create(
                modelClass: Class<T>
            ): T {

                return AniListViewModel(
                    context
                ) as T

            }

        }

    )



    LaunchedEffect(Unit) {

        viewModel.loadUserAnimeList(
            token
        )

    }



    val animeList by viewModel.animeList.collectAsState()



    // Same logic as MyList

    LaunchedEffect(animeList) {


        animeList.forEach { anime ->


            Log.d(
                "SEARCH_LOAD_ANILIST",
                "${anime.title} id=${anime.id}"
            )


            aniListViewModel.loadAiredEpisodes(

                anime.id

            )


        }

    }



    val myList by viewModel.userAnimeList.collectAsState()



    var query by remember {

        mutableStateOf("")

    }



    LaunchedEffect(query) {


        if(query.length >= 3) {


            viewModel.search(

                token = token,

                query = query

            )


        }

    }



    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 40.dp,
                start = 8.dp,
                end = 8.dp
            ),

        horizontalAlignment = Alignment.CenterHorizontally

    ) {



        OutlinedTextField(

            value = query,

            onValueChange = {

                query = it

            },

            label = {

                Text("Search Anime")

            },

            modifier = Modifier.fillMaxWidth()

        )



        Spacer(
            modifier = Modifier.height(16.dp)
        )



        Button(

            onClick = {


                if(query.isNotBlank()) {


                    viewModel.search(

                        token = token,

                        query = query

                    )

                }

            }

        ) {

            Text("Search Anime")

        }



        Spacer(
            modifier = Modifier.height(20.dp)
        )



        LazyColumn(

            modifier = Modifier.fillMaxWidth()

        ) {



            items(animeList) { anime ->



                val airedEpisode by derivedStateOf {

                    aniListViewModel.airedEpisodesMap[anime.id]

                }



                Log.d(
                    "SEARCH_ANILIST_CHECK",
                    "${anime.title} id=${anime.id} aired=$airedEpisode"
                )



                AnimeCard(

                    anime = anime,

                    onClick = {

                        navController.navigate(
                            "details/${anime.id}"
                        )

                    },

                    userAnimeList = myList,

                    airedEpisode =
                        aniListViewModel.airedEpisodesMap[anime.id],

                    aniListSuccess =
                        aniListViewModel.aniListStatusMap[anime.id]

                )


            }


        }


    }


}