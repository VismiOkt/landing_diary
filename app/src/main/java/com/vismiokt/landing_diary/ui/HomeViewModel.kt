package com.vismiokt.landing_diary.ui

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vismiokt.landing_diary.data.CategoryPlant
import com.vismiokt.landing_diary.data.FilterPlant
import com.vismiokt.landing_diary.data.Plant
import com.vismiokt.landing_diary.data.PlantsRepository
import com.vismiokt.landing_diary.data.ResultPlant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class HomeViewModel(private val plantsRepository: PlantsRepository,
                    savedStateHandle: SavedStateHandle
) : ViewModel() {
 //   val plants: List<Plant> = listOf(Plant(1, "tutu"))

//    val homeUiState: StateFlow<HomeUiState> = plantsRepository.getAllPlants().map { HomeUiState(it) }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000L),
//        initialValue = HomeUiState()
//    )
 //   val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    var homeUiState by mutableStateOf(HomeUiState())
        private set

    init {
        viewModelScope.launch {
            homeUiState = HomeUiState(
                plants = plantsRepository.getAllPlants(),
            )
        }
    }

    fun onClickValueResult(result: ResultPlant) {
        viewModelScope.launch {
            homeUiState = homeUiState.copy(
                onFilterResult = true,
                filterResult = result,
                plants = if (homeUiState.onFilterCategory) plantsRepository.getPlantsByResultAndCategory(result, homeUiState.filterCategory!!) else plantsRepository.getPlantsByResult(result)
            )
        }
    }

    fun onClickValueCategory(category: CategoryPlant) {
        viewModelScope.launch {
            homeUiState = homeUiState.copy(
                onFilterCategory = true,
                filterCategory = category,
                plants = if (homeUiState.onFilterResult) plantsRepository.getPlantsByResultAndCategory(homeUiState.filterResult!!, category) else plantsRepository.getPlantsByCategory(category))
        }
    }

    fun deleteFilterCategory() {
        homeUiState = homeUiState.copy(
            onFilterCategory = false,
            filterCategory = null,
            plants = if (homeUiState.onFilterResult) plantsRepository.getPlantsByResult(homeUiState.filterResult!!) else plantsRepository.getAllPlants())
    }

    fun deleteFilterResult() {
        homeUiState = homeUiState.copy(
            onFilterResult = false,
            filterResult = null,
            plants = if (homeUiState.onFilterCategory) plantsRepository.getPlantsByCategory(homeUiState.filterCategory!!) else plantsRepository.getAllPlants())
    }





}

data class HomeUiState(
    val plants: Flow<List<Plant>> = MutableStateFlow(listOf()),
//    val filterList: Boolean = false,
    val onFilterCategory: Boolean = false,
    val onFilterResult: Boolean = false,
    val filterResult: ResultPlant? = null,
    val filterCategory: CategoryPlant? = null
)