package com.nikhil.malclient.utils

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

object PkceUtil {

    /**
     * Generates a secure random code verifier.
     */
    fun generateCodeVerifier(): String {
        val allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"
        val random = SecureRandom()

        return buildString(64) {
            repeat(64) {
                append(allowed[random.nextInt(allowed.length)])
            }
        }
    }

    /**
     * Converts the verifier into a SHA256 challenge.
     */
    fun generateCodeChallenge(verifier: String): String {

        val bytes = verifier.toByteArray(Charsets.US_ASCII)

        val digest = MessageDigest
            .getInstance("SHA-256")
            .digest(bytes)

        return Base64.encodeToString(
            digest,
            Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        )
    }
}