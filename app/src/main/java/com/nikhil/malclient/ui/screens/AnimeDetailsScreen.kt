package com.nikhil.malclient.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nikhil.malclient.viewmodel.AnimeViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import android.util.Log

@Composable
fun AnimeDetailsScreen(
    animeId: String,
    token: String
) {

    val viewModel: AnimeViewModel = viewModel()

    val anime by viewModel.selectedAnime.collectAsState()

    LaunchedEffect(animeId) {
        viewModel.loadAnimeDetails(
            token,
            animeId
        )
    }

    if (anime == null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            AsyncImage(
                model = anime!!.main_picture?.large
                    ?: anime!!.main_picture?.medium,
                contentDescription = anime!!.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = anime!!.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "⭐ Score: ${anime!!.mean ?: "N/A"}"
            )

            Text(
                text = "📺 Episodes: ${anime!!.num_episodes ?: "Unknown"}"
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "🎭 Genres: ${
                    anime!!.genres?.joinToString(", ") {
                        it.name
                    } ?: "Unknown"
                }"
            )

            Text(
                text = "📡 Status: ${anime!!.status ?: "Unknown"}"
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = {

                    Log.d(
                        "MAL_LIST",
                        "Button clicked token=$token animeId=$animeId"
                    )

                    viewModel.addAnimeToList(
                        token = token,
                        animeId = animeId
                    )
                }
            ) {
                Text("Add to My List")
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = anime!!.synopsis ?: "No synopsis"
            )
        }
    }
}