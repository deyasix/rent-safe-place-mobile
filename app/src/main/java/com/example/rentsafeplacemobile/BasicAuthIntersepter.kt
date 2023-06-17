package com.example.rentsafeplacemobile

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor() : Interceptor {
    private var credentials: String? = null
    var isUserLoggedIn: Boolean = false
        private set

    fun setCredentials(username: String, password: String) {
        val credentialsString = "$username:$password"
        val credentialsEncoded = Base64.encodeToString(credentialsString.toByteArray(), Base64.NO_WRAP)
        credentials = "Basic $credentialsEncoded"
        isUserLoggedIn = true
    }

    fun clearCredentials() {
        credentials = null
        isUserLoggedIn = false
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = credentials?.let { credentials ->
            request.newBuilder()
                .header("Authorization", credentials)
                .build()
        } ?: request

        return chain.proceed(authenticatedRequest)
    }
}