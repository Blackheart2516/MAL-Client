package com.nikhil.malclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

@Composable
fun HomeScreen(
    username: String,
    picture: String?,
    onSearchClick: () -> Unit,
    onMyListClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            model = picture,
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Text(text = "Welcome $username")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSearchClick
        ) {
            Text("Search Anime")
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Button(
            onClick = onMyListClick
        ) {
            Text("My List")
        }
    }
}