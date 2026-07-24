package com.nikhil.malclient

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.nikhil.malclient.api.ApiConstants
import com.nikhil.malclient.auth.AuthRepository
import com.nikhil.malclient.auth.OAuthManager
import com.nikhil.malclient.auth.TokenManager
import com.nikhil.malclient.navigation.AppNavigation
import com.nikhil.malclient.ui.theme.MALClientTheme
import kotlinx.coroutines.launch
import com.nikhil.malclient.user.UserRepository
import com.nikhil.malclient.user.UserSession

class MainActivity : ComponentActivity() {

    private lateinit var oauthManager: OAuthManager
    private lateinit var tokenManager: TokenManager
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oauthManager = OAuthManager(this)
        tokenManager = TokenManager(this)

        handleOAuthCallback(intent)

        enableEdgeToEdge()

        if (tokenManager.isLoggedIn()) {
            UserSession.username = tokenManager.getUsername()
            UserSession.picture = tokenManager.getPicture()

            Log.d("MAL_PROFILE", "Stored username = ${tokenManager.getUsername()}")
        }

        setContent {
            MALClientTheme {
                AppNavigation(
                    clientId = ApiConstants.CLIENT_ID,
                    startDestination = if (tokenManager.isLoggedIn()) "home" else "login"
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleOAuthCallback(intent)
    }

    private fun handleOAuthCallback(intent: Intent?) {

        val data: Uri? = intent?.data

        if (data?.scheme == "malclient" && data.host == "callback") {

            val code = data.getQueryParameter("code")

            if (code == null) {
                Toast.makeText(this, "Authorization failed", Toast.LENGTH_LONG).show()
                return
            }

            val verifier = oauthManager.getCodeVerifier()

            if (verifier == null) {
                throw RuntimeException("Verifier = $verifier")
                return
            }

            lifecycleScope.launch {
                val result = authRepository.exchangeCodeForToken(
                    code,
                    verifier
                )

                result.onSuccess { token ->

                    tokenManager.saveTokens(
                        token.access_token,
                        token.refresh_token
                    )

                    oauthManager.clearCodeVerifier()

                    val response = userRepository.getMyProfile(token.access_token)

                    if (response.isSuccessful && response.body() != null) {

                        UserSession.username = response.body()!!.name
                        UserSession.picture = response.body()!!.picture

                        tokenManager.saveUserProfile(
                            UserSession.username,
                            UserSession.picture
                        )

                        Log.d("MAL_PROFILE", "Saved username = ${tokenManager.getUsername()}")

                        Toast.makeText(
                            this@MainActivity,
                            "Welcome ${UserSession.username}",
                            Toast.LENGTH_LONG
                        ).show()
                    }


                    Log.d("MAL_AUTH", "Access Token: ${token.access_token}")

                    Toast.makeText(
                        this@MainActivity,
                        "Login Successful!",
                        Toast.LENGTH_LONG
                    ).show()

                    // Later we'll navigate directly to the home/search screen.
                }

                result.onFailure { error ->

                    error.printStackTrace()

                    Log.e("MAL_AUTH", "Exception Class: ${error::class.java.name}")
                    Log.e("MAL_AUTH", "Exception: ", error)

                    Toast.makeText(
                        this@MainActivity,
                        error.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}