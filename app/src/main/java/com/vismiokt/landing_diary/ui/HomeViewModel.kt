package com.vismiokt.landing_diary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vismiokt.landing_diary.data.Plant
import com.vismiokt.landing_diary.data.PlantsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class HomeViewModel(plantsRepository: PlantsRepository) : ViewModel() {
 //   val plants: List<Plant> = listOf(Plant(1, "tutu"))

    val homeUiState: StateFlow<HomeUiState> = plantsRepository.getAllPlants().map { HomeUiState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeUiState()
    )
 //   val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()





}

data class HomeUiState(
    val plants: List<Plant> = listOf()
)