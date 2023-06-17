package com.example.rentsafeplacemobile

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8088/"


    val basicAuthInterceptor = BasicAuthInterceptor()

    private val retrofit: Retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(basicAuthInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    fun setAuthCredentials(username: String, password: String) {
        basicAuthInterceptor.setCredentials(username, password)
    }
    fun logout() {
        basicAuthInterceptor.clearCredentials()
    }

}