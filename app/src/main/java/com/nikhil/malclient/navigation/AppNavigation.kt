package com.nikhil.malclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nikhil.malclient.auth.TokenManager
import com.nikhil.malclient.ui.screens.AnimeDetailsScreen
import com.nikhil.malclient.ui.screens.HomeScreen
import com.nikhil.malclient.ui.screens.LoginScreen
import com.nikhil.malclient.ui.screens.MyListScreen
import com.nikhil.malclient.ui.screens.SearchScreen
import com.nikhil.malclient.user.UserSession

@Composable
fun AppNavigation(
    clientId: String,
    startDestination: String = "login"
) {

    val navController = rememberNavController()

    val context = LocalContext.current
    val tokenManager = TokenManager(context)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // Login Screen
        composable("login") {
            LoginScreen()
        }


        // Home Screen
        composable("home") {

            HomeScreen(
                username = UserSession.username,
                picture = UserSession.picture,

                onSearchClick = {
                    navController.navigate("search")
                },

                onMyListClick = {
                    navController.navigate("mylist")
                }
            )
        }


        // Search Screen
        composable("search") {

            SearchScreen(
                token = tokenManager.getAccessToken() ?: "",
                navController = navController
            )
        }


        // My List Screen
        composable("mylist") {

            MyListScreen(
                token = tokenManager.getAccessToken() ?: ""
            )
        }


        // Anime Details Screen
        composable("details/{animeId}") { backStackEntry ->

            val animeId =
                backStackEntry.arguments?.getString("animeId") ?: ""

            AnimeDetailsScreen(
                animeId = animeId,
                token = tokenManager.getAccessToken() ?: ""
            )
        }
    }
}