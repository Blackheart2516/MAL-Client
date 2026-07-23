package com.nikhil.malclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nikhil.malclient.ui.screens.AnimeDetailsScreen
import com.nikhil.malclient.ui.screens.SearchScreen

@Composable
fun AppNavigation(clientId: String) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {

        composable("search") {
            SearchScreen(
                clientId = clientId,
                navController = navController
            )
        }

        composable("details/{animeId}") { backStackEntry ->

            val animeId =
                backStackEntry.arguments?.getString("animeId") ?: ""

            AnimeDetailsScreen(
                animeId = animeId,
                clientId = clientId
            )
        }
    }
}