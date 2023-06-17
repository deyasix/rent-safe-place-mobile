package com.example.rentsafeplacemobile.utilits.ext

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("PREFERENCES")

suspend fun Context.saveData(key: String, value: String) {
    dataStore.edit { settings ->
        settings[stringPreferencesKey(key)] = value
    }
}

suspend fun Context.getDataValue(key: String): String {
    return dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(key)]
    }.first()?:""
}