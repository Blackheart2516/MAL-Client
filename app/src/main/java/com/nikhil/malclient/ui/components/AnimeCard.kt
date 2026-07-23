package com.nikhil.malclient.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.nikhil.malclient.model.Anime

@Composable
fun AnimeCard(
    anime: Anime,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Row(
            modifier = Modifier.padding(12.dp)
        ) {

            Image(
                painter = rememberAsyncImagePainter(anime.main_picture?.medium),
                contentDescription = anime.title,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Text(
                    text = anime.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("⭐ Rating: ${anime.mean ?: "N/A"}")

                Text("📺 Episodes: ${anime.num_episodes ?: "Unknown"}")

                Text("📡 Status: ${anime.status ?: "Unknown"}")
            }
        }
    }
}