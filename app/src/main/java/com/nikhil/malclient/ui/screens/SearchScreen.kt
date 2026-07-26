package com.nikhil.malclient.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nikhil.malclient.viewmodel.AnimeSearchViewModel
import androidx.compose.runtime.collectAsState
import com.nikhil.malclient.ui.components.AnimeCard
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import android.util.Log

@Composable
fun SearchScreen(
    token: String,
    navController: androidx.navigation.NavController
){
    val viewModel: AnimeSearchViewModel = viewModel()

    LaunchedEffect(Unit) {

        viewModel.loadUserAnimeList(token)

    }
    val animeList by viewModel.animeList.collectAsState()
    Log.d(
        "SEARCH_SIZE",
        "results = ${animeList.size}"
    )
    val myList by viewModel.userAnimeList.collectAsState()
    var query by remember {
        mutableStateOf("")
    }
    LaunchedEffect(query) {

        if (query.length >= 3) {

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
                if (query.isNotBlank()) {
                    viewModel.search(
                        token = token,
                        query = query
                    )
                }
            }

        ) {
            Text("Search")
        }
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {

            items(animeList) { anime ->

                AnimeCard(
                    anime = anime,
                    onClick = {
                        navController.navigate("details/${anime.id}")
                    },
                    userAnimeList = myList
                )
            }
        }

    }
}