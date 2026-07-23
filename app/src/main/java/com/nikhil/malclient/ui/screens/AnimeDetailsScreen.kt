package com.nikhil.malclient.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nikhil.malclient.viewmodel.AnimeViewModel

@Composable
fun AnimeDetailsScreen(
    animeId: String,
    clientId: String
) {
    val viewModel: AnimeViewModel = viewModel()
    val anime by viewModel.selectedAnime.collectAsState()

    LaunchedEffect(animeId) {
        viewModel.loadAnimeDetails(clientId, animeId)
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
        ) {

            AsyncImage(
                model = anime!!.main_picture?.large ?: anime!!.main_picture?.medium,
                contentDescription = anime!!.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = anime!!.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("⭐ Rating: ${anime!!.mean ?: "N/A"}")

            Text("📺 Episodes: ${anime!!.num_episodes ?: "Unknown"}")

            Text("📡 Status: ${anime!!.status ?: "Unknown"}")

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = anime!!.synopsis ?: "No synopsis available."
            )
        }
    }
}