package com.nikhil.malclient

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import com.nikhil.malclient.api.ApiConstants
import com.nikhil.malclient.auth.AuthRepository
import com.nikhil.malclient.auth.OAuthManager
import com.nikhil.malclient.auth.TokenManager
import com.nikhil.malclient.cache.AppCache
import com.nikhil.malclient.navigation.AppNavigation
import com.nikhil.malclient.ui.theme.MALClientTheme
import com.nikhil.malclient.user.UserRepository
import com.nikhil.malclient.user.UserSession
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {


    private lateinit var oauthManager: OAuthManager

    private lateinit var tokenManager: TokenManager

    private lateinit var appCache: AppCache


    private val loginState =
        mutableStateOf(false)



    private val authRepository =
        AuthRepository()


    private val userRepository =
        UserRepository()





    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        oauthManager =
            OAuthManager(this)


        tokenManager =
            TokenManager(this)


        appCache =
            AppCache(this)



        handleOAuthCallback(intent)



        enableEdgeToEdge()




        // Load cached profile

        if (tokenManager.isLoggedIn()) {


            loginState.value = true



            val cachedUsername =
                appCache.getUsername()


            val cachedPicture =
                appCache.getPicture()



            if (cachedUsername.isNotEmpty()) {


                UserSession.username.value =
                    cachedUsername


                UserSession.picture.value =
                    cachedPicture



                Log.d(
                    "MAL_PROFILE_CACHE",
                    "Loaded username=$cachedUsername"
                )

            }

        }






        setContent {


            MALClientTheme {


                AppNavigation(

                    clientId = ApiConstants.CLIENT_ID,


                    startDestination =
                        if (loginState.value)
                            "home"
                        else
                            "login",


                    onLoginSuccess = {

                        loginState.value = true

                    }

                )

            }

        }

    }







    override fun onNewIntent(intent: Intent) {

        super.onNewIntent(intent)

        handleOAuthCallback(intent)

    }







    private fun handleOAuthCallback(intent: Intent?) {


        val data: Uri? =
            intent?.data




        if (
            data?.scheme == "malclient" &&
            data.host == "callback"
        ) {



            val code =
                data.getQueryParameter("code")



            if (code == null) {

                Toast.makeText(
                    this,
                    "Authorization failed",
                    Toast.LENGTH_LONG
                ).show()

                return

            }





            val verifier =
                oauthManager.getCodeVerifier()



            if (verifier == null) {

                Toast.makeText(
                    this,
                    "Verifier missing",
                    Toast.LENGTH_LONG
                ).show()

                return

            }







            lifecycleScope.launch {



                val result =
                    authRepository.exchangeCodeForToken(
                        code,
                        verifier
                    )






                result.onSuccess { token ->



                    tokenManager.saveTokens(

                        token.access_token,

                        token.refresh_token

                    )



                    // Update Compose state

                    loginState.value = true




                    oauthManager.clearCodeVerifier()






                    val response =
                        userRepository.getMyProfile(
                            token.access_token
                        )






                    if (
                        response != null &&
                        response.isSuccessful &&
                        response.body() != null
                    ) {



                        val user =
                            response.body()!!



                        UserSession.username.value =
                            user.name



                        UserSession.picture.value =
                            user.picture





                        // Save profile cache

                        appCache.saveUsername(

                            UserSession.username.value

                        )


                        appCache.savePicture(

                            UserSession.picture.value

                        )





                        Log.d(
                            "MAL_PROFILE",
                            "Saved username=${UserSession.username.value}"
                        )





                        Toast.makeText(

                            this@MainActivity,

                            "Welcome ${UserSession.username.value}",

                            Toast.LENGTH_LONG

                        ).show()


                    }





                    Log.d(
                        "MAL_AUTH",
                        "Login Successful"
                    )


                }







                result.onFailure { error ->



                    Log.e(
                        "MAL_AUTH",
                        "Login failed",
                        error
                    )



                    Toast.makeText(

                        this@MainActivity,

                        error.message ?: "Login failed",

                        Toast.LENGTH_LONG

                    ).show()


                }



            }



        }



    }



}