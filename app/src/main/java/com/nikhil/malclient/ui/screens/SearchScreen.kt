package com.nikhil.malclient.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import com.nikhil.malclient.utils.SearchHistoryManager
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
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

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val searchHistoryManager =
        remember {
            SearchHistoryManager(context)
        }


    var searchHistory by remember {

        mutableStateOf(
            searchHistoryManager.getHistory()
        )

    }
    val keyboardController =
        androidx.compose.ui.platform.LocalSoftwareKeyboardController.current

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

            modifier = Modifier.fillMaxWidth(),


            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),


            keyboardActions = KeyboardActions(

                onSearch = {


                    keyboardController?.hide()


                    if(query.isNotBlank()) {


                        val searchText =
                            query.trim()



                        searchHistoryManager.saveSearch(
                            searchText
                        )


                        searchHistory =
                            searchHistoryManager.getHistory()



                        viewModel.search(

                            token = token,

                            query = searchText

                        )


                    }


                }

            )


        )

        if(query.isEmpty() && searchHistory.isNotEmpty()) {


            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp
                    )

            ) {


                Row(

                    modifier = Modifier.fillMaxWidth(),

                    horizontalArrangement = Arrangement.SpaceBetween,

                    verticalAlignment = Alignment.CenterVertically

                ) {


                    Text(

                        text = "Recent Searches",

                        style = MaterialTheme.typography.titleMedium

                    )



                    TextButton(

                        onClick = {


                            searchHistoryManager.clearHistory()


                            searchHistory =
                                emptyList()


                        }

                    ) {


                        Text("Clear")

                    }


                }



                searchHistory.forEach { history ->



                    TextButton(

                        onClick = {


                            query = history


                            viewModel.search(

                                token = token,

                                query = history

                            )


                        },

                        modifier = Modifier.fillMaxWidth()

                    ) {


                        Text(

                            text = "🔍 $history",

                            modifier = Modifier.fillMaxWidth()

                        )


                    }


                }


            }


        }



        Spacer(
            modifier = Modifier.height(16.dp)
        )



        Button(

            onClick = {

                if(query.isNotBlank()) {


                    val searchText =
                        query.trim()



                    searchHistoryManager.saveSearch(
                        searchText
                    )


                    searchHistory =
                        searchHistoryManager.getHistory()



                    viewModel.search(

                        token = token,

                        query = searchText

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



                val airedEpisode by remember {

                    derivedStateOf {

                        aniListViewModel.airedEpisodesMap[anime.id]

                    }

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
                        aniListViewModel.aniListStatusMap[anime.id],


                    onAddToList = { selectedAnime ->


                        viewModel.addToList(

                            token = token,

                            animeId = selectedAnime.id.toString(),

                            status = "plan_to_watch"

                        ) { success ->


                            if(success) {


                                viewModel.loadUserAnimeList(
                                    token
                                )


                            }


                        }


                    }

                )


            }


        }

    }


}