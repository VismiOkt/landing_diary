package com.vismiokt.landing_diary.ui

import androidx.lifecycle.ViewModel
import com.vismiokt.landing_diary.data.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
 //   val plants: List<Plant> = listOf(Plant(1, "tutu"))

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()





}

data class HomeUiState(
    val plants: List<Plant> = listOf(Plant(1, "tutu"))
)