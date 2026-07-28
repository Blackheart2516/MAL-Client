package com.nikhil.malclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.nikhil.malclient.viewmodel.MyListViewModel
import androidx.lifecycle.ViewModelProvider



@Composable
fun AppNavigation(
    clientId: String,
    startDestination: String = "login",
    onLoginSuccess: () -> Unit
) {


    val navController =
        rememberNavController()



    val context =
        LocalContext.current


    val tokenManager =
        TokenManager(context)





    val myListViewModel: MyListViewModel = viewModel(

        factory = object : ViewModelProvider.Factory {


            override fun <T : androidx.lifecycle.ViewModel> create(

                modelClass: Class<T>

            ): T {


                return MyListViewModel(
                    context
                ) as T

            }

        }

    )






    NavHost(

        navController = navController,

        startDestination = startDestination

    ) {




        composable("login") {


            LoginScreen(

                onLoginSuccess = {

                    navController.navigate("home") {

                        popUpTo("login") {

                            inclusive = true

                        }

                    }

                }

            )


        }






        composable("home") {



            HomeScreen(


                username =
                    UserSession.username.value,


                picture =
                    UserSession.picture.value,



                onSearchClick = {


                    navController.navigate(
                        "search"
                    )


                },



                onMyListClick = {


                    navController.navigate(
                        "mylist"
                    )


                }


            )


        }






        composable("search") {



            SearchScreen(


                token =
                    tokenManager.getAccessToken()
                        ?: "",


                navController =
                    navController,


                myListViewModel =
                    myListViewModel


            )


        }







        composable("mylist") {



            MyListScreen(


                token =
                    tokenManager.getAccessToken()
                        ?: "",


                myListViewModel =
                    myListViewModel


            )


        }







        composable("details/{animeId}") { backStackEntry ->



            val animeId =
                backStackEntry.arguments
                    ?.getString("animeId")
                    ?: ""



            AnimeDetailsScreen(


                animeId = animeId,


                token =
                    tokenManager.getAccessToken()
                        ?: ""


            )


        }


    }


}