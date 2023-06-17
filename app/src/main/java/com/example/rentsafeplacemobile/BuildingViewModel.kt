package com.example.rentsafeplacemobile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentsafeplacemobile.data.Building
import com.example.rentsafeplacemobile.data.Realtor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BuildingViewModel : ViewModel() {
    private val _uiState = MutableLiveData<MutableList<Building>>(mutableListOf())
    val uiState: LiveData<MutableList<Building>> = _uiState

    private val _averagePriceState = MutableStateFlow<Int?>(null)
    val averagePriceState: StateFlow<Int?> = _averagePriceState.asStateFlow()

    private val _buildingDetails = MutableStateFlow<Building?>(null)
    val buildingDetails: StateFlow<Building?> = _buildingDetails.asStateFlow()

    private val _realtor = MutableStateFlow<Realtor?>(null)
    val realtor: StateFlow<Realtor?> = _realtor.asStateFlow()
    init {
        viewModelScope.launch {
            val buildings = Repository.getBuildings()
            _uiState.postValue(mutableListOf<Building>().apply { addAll(buildings) })
        }
    }

    fun getStatistics(city: String) {
        viewModelScope.launch {
            val averagePrice = Repository.getStatistics(city)
            _averagePriceState.value = averagePrice
        }
    }

    fun getBuildingDetails(id: Long) {
        viewModelScope.launch {
            val building = Repository.getBuilding(id)
            _buildingDetails.value = building
        }
    }

    fun getRealtor(id: Long) {
        viewModelScope.launch {
            val realtor = Repository.getRealtor(id)
            _realtor.value = realtor
        }
    }

}