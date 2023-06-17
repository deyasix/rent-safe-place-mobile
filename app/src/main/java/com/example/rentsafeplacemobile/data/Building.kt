package com.example.rentsafeplacemobile.data

data class Building(
    val id: Long,
    val realtor: Realtor,
    val tenant: Tenant,
    val type: String,
    val square: Int,
    val name: String,
    val price: Int,
    val isPetAllowed: Boolean,
    val photo: String?,
    val description: String,
    val isLeased: Boolean,
    val category: String,
    val city: String,
    val address: String
)