package com.nikhil.malclient.auth

import com.nikhil.malclient.api.ApiConstants
import com.nikhil.malclient.api.RetrofitClient

class AuthRepository {

    suspend fun exchangeCodeForToken(
        code: String,
        codeVerifier: String
    ): Result<TokenResponse> {

        return try {

            val response = RetrofitClient.authApi.getAccessToken(
                clientId = ApiConstants.CLIENT_ID,
                code = code,
                codeVerifier = codeVerifier,
                redirectUri = OAuthManager.REDIRECT_URI
            )

            if (response.isSuccessful && response.body() != null) {

                Result.success(response.body()!!)

            } else {

                val errorBody = response.errorBody()?.string()

                Result.failure(
                    Exception(
                        """
                        HTTP ${response.code()}
                        $errorBody
                        """.trimIndent()
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}