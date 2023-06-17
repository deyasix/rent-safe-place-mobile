package com.example.rentsafeplacemobile.data

data class Tenant(
    val id: Long?,
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
    val photo: String?
)
