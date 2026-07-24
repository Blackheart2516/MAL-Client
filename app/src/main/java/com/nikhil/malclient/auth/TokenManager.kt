package com.nikhil.malclient.auth

import android.content.Context

class TokenManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("mal_auth", Context.MODE_PRIVATE)

    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"

        private const val KEY_USERNAME = "username"
        private const val KEY_PICTURE = "picture"
    }

    fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        prefs.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .putString(REFRESH_TOKEN, refreshToken)
            .apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }

    fun clearTokens() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }

    fun saveUserProfile(username: String, picture: String?) {
        prefs.edit()
            .putString(KEY_USERNAME, username)
            .putString(KEY_PICTURE, picture)
            .apply()
    }

    fun getUsername(): String {
        return prefs.getString(KEY_USERNAME, "") ?: ""
    }

    fun getPicture(): String? {
        return prefs.getString(KEY_PICTURE, null)
    }

    fun clearUserProfile() {
        prefs.edit()
            .remove(KEY_USERNAME)
            .remove(KEY_PICTURE)
            .apply()
    }
}