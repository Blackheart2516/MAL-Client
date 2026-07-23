package com.nikhil.malclient.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nikhil.malclient.viewmodel.AnimeViewModel
import com.nikhil.malclient.ui.components.AnimeCard

@Composable
fun SearchScreen(
    clientId: String,
    navController: androidx.navigation.NavHostController
) {
    val viewModel: AnimeViewModel = viewModel()
    val animeResponse by viewModel.anime.collectAsState()

    var query by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Search Anime")
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (query.isNotBlank()) {
                    viewModel.searchAnime(clientId, query)
                }
            }
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            animeResponse?.data?.let { list ->
                items(list) { anime ->
                    AnimeCard(
                        anime = anime.node,
                        onClick = {
                            navController.navigate("details/${anime.node.id}")
                        }
                    )
                }
            }
        }
    }
}