package com.example.rentsafeplacemobile

import com.example.rentsafeplacemobile.data.Building
import com.example.rentsafeplacemobile.data.Realtor
import com.example.rentsafeplacemobile.data.Tenant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginData(val email: String, val password: String)

interface ApiService {
    @GET("tenants/buildings")
    suspend fun getBuildings(): Response<List<Building>>

    @GET("tenants/buildings/statistics/{city}")
    suspend fun getStatistics(@Path("city") city: String): Response<Int>

    @GET("tenants/buildings/{id}")
    suspend fun getBuilding(@Path("id") id: Long): Response<Building>

    @POST("tenants/login")
    suspend fun login(@Body requestData: LoginData): Response<Tenant>

    @GET("tenants/info")
    suspend fun getInfo(): Response<Tenant>

    @PUT("tenants/info")
    suspend fun editInfo(@Body tenant: Tenant): Response<Tenant>

    @POST("tenants/register")
    suspend fun register(@Body tenant: Tenant): Response<Tenant>

    @POST("tenants/logout")
    suspend fun logout(): Response<String>

    @GET("tenants/realtors/{id}")
    suspend fun getRealtor(@Path("id") id: Long) : Response<Realtor>
}