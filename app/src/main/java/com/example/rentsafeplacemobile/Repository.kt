package com.example.rentsafeplacemobile

import com.example.rentsafeplacemobile.data.Building
import com.example.rentsafeplacemobile.data.Realtor
import com.example.rentsafeplacemobile.data.Tenant


object Repository {
    private val apiService: ApiService = RetrofitClient.apiService

    suspend fun getBuildings(): List<Building> {
        try {
            val response = apiService.getBuildings()
            if (response.isSuccessful) {
                return response.body()?.toList() ?: emptyList()
            }
        } catch (_: Exception) { }
        return emptyList()
    }

    suspend fun getStatistics(city: String) : Int? {
        try {
            val response = apiService.getStatistics(city)
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (_: Exception) {

        }
        return null
    }

    suspend fun getBuilding(id: Long) : Building? {
        try {
            val response = apiService.getBuilding(id)
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (_: Exception) {

        }
        return null
    }

    suspend fun getInfo() : Tenant? {
        try {
            val response = apiService.getInfo()
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (_: Exception) {

        }
        return null
    }

    suspend fun editInfo(tenant: Tenant) : Tenant? {
        try {
            val response = apiService.editInfo(tenant)
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (_: Exception) {

        }
        return null
    }

    suspend fun logout(): String {
        try {
            val response = apiService.logout()
            if (response.isSuccessful) {
                return response.body()?:""
            }
        } catch (_: Exception) {

        }
        return ""
    }

    suspend fun getRealtor(id: Long) : Realtor? {
        try {
            val response = apiService.getRealtor(id)
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (_: Exception) {

        }
        return null
    }

}