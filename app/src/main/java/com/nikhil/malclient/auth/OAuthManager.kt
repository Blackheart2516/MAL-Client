package com.nikhil.malclient.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.nikhil.malclient.api.ApiConstants
import com.nikhil.malclient.utils.PkceUtil

class OAuthManager(private val context: Context) {

    companion object {
        const val REDIRECT_URI = "malclient://callback"

        private const val PREF_NAME = "oauth_prefs"
        private const val KEY_CODE_VERIFIER = "code_verifier"
    }

    private val prefs =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun login() {

        val codeVerifier = PkceUtil.generateCodeVerifier()

        prefs.edit()
            .putString(KEY_CODE_VERIFIER, codeVerifier)
            .apply()

        val codeChallenge = codeVerifier

        val authUrl =
            "https://myanimelist.net/v1/oauth2/authorize" +
                    "?response_type=code" +
                    "&client_id=${ApiConstants.CLIENT_ID}" +
                    "&redirect_uri=$REDIRECT_URI" +
                    "&code_challenge=$codeChallenge" +
                    "&code_challenge_method=plain"

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(authUrl)
        )

        context.startActivity(intent)
    }

    fun getCodeVerifier(): String? {
        return prefs.getString(KEY_CODE_VERIFIER, null)
    }

    fun clearCodeVerifier() {
        prefs.edit()
            .remove(KEY_CODE_VERIFIER)
            .apply()
    }
}