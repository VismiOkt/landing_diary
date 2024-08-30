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
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


class HomeViewModel(
    private val plantsRepository: PlantsRepository,
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
                plantsYear = plantsRepository.getPlantsDate()
                    .map { it.map { date -> date.substring(0, 4) }.distinct() }
            )
        }
    }

    fun onClickValueResult(result: ResultPlant) {
        homeUiState = homeUiState.copy(
            onFilterResult = true,
            filterResult = result,
            plants = if (homeUiState.onFilterCategory && homeUiState.onFilterYear) plantsRepository.getPlantsByResultAndCategoryAndYear(
                result, homeUiState.filterCategory!!, homeUiState.filterYear!!
            ) else if (homeUiState.onFilterCategory && !homeUiState.onFilterYear) plantsRepository.getPlantsByResultAndCategory(
                result, homeUiState.filterCategory!!
            ) else if (!homeUiState.onFilterCategory && homeUiState.onFilterYear) plantsRepository.getPlantsByResultAndYear(
                result, homeUiState.filterYear!!
            ) else plantsRepository.getPlantsByResult(result)
        )

    }

    fun onClickValueCategory(category: CategoryPlant) {
        homeUiState = homeUiState.copy(
            onFilterCategory = true,
            filterCategory = category,
            plants = if (homeUiState.onFilterResult && homeUiState.onFilterYear) plantsRepository.getPlantsByResultAndCategoryAndYear(
                homeUiState.filterResult!!, category, homeUiState.filterYear!!
            ) else if (homeUiState.onFilterResult && !homeUiState.onFilterYear) plantsRepository.getPlantsByResultAndCategory(
                homeUiState.filterResult!!,
                category
            ) else if (!homeUiState.onFilterResult && homeUiState.onFilterYear) plantsRepository.getPlantsByCategoryAndYear(
                category, homeUiState.filterYear!!
            ) else plantsRepository.getPlantsByCategory(category)
        )

    }

    fun onClickValueYear(year: String) {
        homeUiState = homeUiState.copy(
            onFilterYear = true,
            filterYear = year,
            plants = if (homeUiState.onFilterCategory && homeUiState.onFilterResult) plantsRepository.getPlantsByResultAndCategoryAndYear(
                homeUiState.filterResult!!, homeUiState.filterCategory!!, year
            ) else if (homeUiState.onFilterCategory && !homeUiState.onFilterResult) plantsRepository.getPlantsByCategoryAndYear(
                homeUiState.filterCategory!!, year
            ) else if (!homeUiState.onFilterCategory && homeUiState.onFilterResult) plantsRepository.getPlantsByResultAndYear(
                homeUiState.filterResult!!, year
            ) else plantsRepository.getPlantsByYear(year)
        )

    }

    fun deleteFilterCategory() {
        homeUiState = homeUiState.copy(
            onFilterCategory = false,
            filterCategory = null,

            plants = if (homeUiState.onFilterResult && homeUiState.onFilterYear) plantsRepository.getPlantsByResultAndYear(
                homeUiState.filterResult!!, homeUiState.filterYear!!
            ) else if (homeUiState.onFilterResult && !homeUiState.onFilterYear) plantsRepository.getPlantsByResult(
                homeUiState.filterResult!!
            ) else if (!homeUiState.onFilterResult && homeUiState.onFilterYear) plantsRepository.getPlantsByYear(
                homeUiState.filterYear!!
            ) else plantsRepository.getAllPlants()
        )
    }

    fun deleteFilterResult() {
        homeUiState = homeUiState.copy(
            onFilterResult = false,
            filterResult = null,
            plants = if (homeUiState.onFilterCategory && homeUiState.onFilterYear) plantsRepository.getPlantsByCategoryAndYear(
                homeUiState.filterCategory!!, homeUiState.filterYear!!
            ) else if (homeUiState.onFilterCategory && !homeUiState.onFilterYear) plantsRepository.getPlantsByCategory(
                homeUiState.filterCategory!!
            ) else if (!homeUiState.onFilterCategory && homeUiState.onFilterYear) plantsRepository.getPlantsByYear(
                homeUiState.filterYear!!
            ) else plantsRepository.getAllPlants()
        )
    }

    fun deleteFilterYear() {
        homeUiState = homeUiState.copy(
            onFilterYear = false,
            filterYear = null,
            plants = if (homeUiState.onFilterCategory && homeUiState.onFilterResult) plantsRepository.getPlantsByResultAndCategory(
                homeUiState.filterResult!!, homeUiState.filterCategory!!
            ) else if (homeUiState.onFilterCategory && !homeUiState.onFilterResult) plantsRepository.getPlantsByCategory(
                homeUiState.filterCategory!!
            ) else if (!homeUiState.onFilterCategory && homeUiState.onFilterResult) plantsRepository.getPlantsByResult(
                homeUiState.filterResult!!
            ) else plantsRepository.getAllPlants()
        )
    }


}

data class HomeUiState(
    val plants: Flow<List<Plant>> = MutableStateFlow(listOf()),
    val onFilterCategory: Boolean = false,
    val onFilterResult: Boolean = false,
    val onFilterYear: Boolean = false,
    val filterResult: ResultPlant? = null,
    val filterCategory: CategoryPlant? = null,
    val filterYear: String? = null,
    val plantsYear: Flow<List<String>> = MutableStateFlow(listOf())
)