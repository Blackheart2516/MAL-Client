package com.nikhil.malclient.auth

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiService {

    @FormUrlEncoded
    @POST("v1/oauth2/token")
    suspend fun getAccessToken(

        @Field("client_id")
        clientId: String,

        @Field("code")
        code: String,

        @Field("code_verifier")
        codeVerifier: String,

        @Field("grant_type")
        grantType: String = "authorization_code",

        @Field("redirect_uri")
        redirectUri: String = "malclient://callback"

    ): Response<TokenResponse>
}