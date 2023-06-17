package com.example.rentsafeplacemobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentsafeplacemobile.data.Tenant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InfoViewModel : ViewModel() {
    private val _info = MutableStateFlow<Tenant?>(null)
    val info: StateFlow<Tenant?> = _info.asStateFlow()
    fun getInfo() {
        viewModelScope.launch {
            val info = Repository.getInfo()
            _info.value = info
        }
    }
    fun editInfo(tenant: Tenant) {
        viewModelScope.launch {
            val info = Repository.editInfo(tenant)

            if (info != null) {
                _info.value = info
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            val result = Repository.logout()
            if (result != "") {
                _info.value = null
            }
        }
    }
}