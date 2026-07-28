package com.nikhil.malclient.user

import androidx.compose.runtime.mutableStateOf

object UserSession {

    var username = mutableStateOf("")

    var picture = mutableStateOf<String?>(null)

}