package com.vismiokt.landing_diary.ui.view_model

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vismiokt.landing_diary.data.CategoryPlant
import com.vismiokt.landing_diary.data.Plant
import com.vismiokt.landing_diary.data.PlantsRepository
import com.vismiokt.landing_diary.data.ResultPlant
import com.vismiokt.landing_diary.domain.doesMatchSearchQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class HomeViewModel(
    private val plantsRepository: PlantsRepository
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _plants = MutableStateFlow(listOf<Plant>())
    private val plants = _plants.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _resPlants = MutableStateFlow(listOf<Plant>())
    val resPlants = _resPlants.asStateFlow()

    private val _plantsYear = MutableStateFlow(listOf<String>())
    val plantsYear = _plantsYear.asStateFlow()

    init {
        viewModelScope.launch {
            plantsRepository.getPlantsDate().map { list ->
                list.map { date -> date.substring(0, 4) }.distinct()
            }.collect {
                _plantsYear.value = it
            }

        }
        viewModelScope.launch {
            plantsRepository.getAllPlants().collect {
                _plants.value = it
            }
        }
        searchText.combine(plants) { text, plants ->
            _resPlants.value = plants.filter {
                it.doesMatchSearchQuery(text)
            }
        }.launchIn(viewModelScope)
    }


    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onClearSearchText() {
        if (_searchText.value.isBlank()) {
            _homeUiState.update {
                it.copy(showBarSearch = false)
            }
        } else {
            _searchText.value = ""
        }
    }

    fun onSearchBar() {
        _homeUiState.update {
            it.copy(showBarSearch = !homeUiState.value.showBarSearch)
        }
        _searchText.value = ""
    }

    fun onClickValueResult(result: ResultPlant) {
        viewModelScope.launch {
            if (homeUiState.value.onFilterCategory && homeUiState.value.onFilterYear) plantsRepository.getPlantsByResultAndCategoryAndYear(
                result, homeUiState.value.filterCategory!!, homeUiState.value.filterYear!!
            ) else if (homeUiState.value.onFilterCategory && !homeUiState.value.onFilterYear) plantsRepository.getPlantsByResultAndCategory(
                result, homeUiState.value.filterCategory!!
            ) else if (!homeUiState.value.onFilterCategory && homeUiState.value.onFilterYear) plantsRepository.getPlantsByResultAndYear(
                result, homeUiState.value.filterYear!!
            ) else plantsRepository.getPlantsByResult(result)
                .collect {
                    _plants.value = it
                }
        }

        _homeUiState.update {
            it.copy(
                onFilterResult = true,
                filterResult = result,
            )
        }

    }

    fun onClickValueCategory(category: CategoryPlant) {
        viewModelScope.launch {
            if (homeUiState.value.onFilterResult && homeUiState.value.onFilterYear) plantsRepository.getPlantsByResultAndCategoryAndYear(
                homeUiState.value.filterResult!!, category, homeUiState.value.filterYear!!
            ) else if (homeUiState.value.onFilterResult && !homeUiState.value.onFilterYear) plantsRepository.getPlantsByResultAndCategory(
                homeUiState.value.filterResult!!,
                category
            ) else if (!homeUiState.value.onFilterResult && homeUiState.value.onFilterYear) plantsRepository.getPlantsByCategoryAndYear(
                category, homeUiState.value.filterYear!!
            ) else plantsRepository.getPlantsByCategory(category)
                .collect {
                    _plants.value = it
                }
        }
        _homeUiState.update {
            it.copy(
                onFilterCategory = true,
                filterCategory = category,
            )
        }

    }

    fun onClickValueYear(year: String) {
        viewModelScope.launch {
            if (homeUiState.value.onFilterCategory && homeUiState.value.onFilterResult) plantsRepository.getPlantsByResultAndCategoryAndYear(
                homeUiState.value.filterResult!!, homeUiState.value.filterCategory!!, year
            ) else if (homeUiState.value.onFilterCategory && !homeUiState.value.onFilterResult) plantsRepository.getPlantsByCategoryAndYear(
                homeUiState.value.filterCategory!!, year
            ) else if (!homeUiState.value.onFilterCategory && homeUiState.value.onFilterResult) plantsRepository.getPlantsByResultAndYear(
                homeUiState.value.filterResult!!, year
            ) else plantsRepository.getPlantsByYear(year)
                .collect {
                    _plants.value = it
                }
        }
        _homeUiState.update {
            it.copy(
                onFilterYear = true,
                filterYear = year,
            )
        }

    }

    fun deleteFilterCategory() {
        viewModelScope.launch {
            if (homeUiState.value.onFilterResult && homeUiState.value.onFilterYear) plantsRepository.getPlantsByResultAndYear(
                homeUiState.value.filterResult!!, homeUiState.value.filterYear!!
            ) else if (homeUiState.value.onFilterResult && !homeUiState.value.onFilterYear) plantsRepository.getPlantsByResult(
                homeUiState.value.filterResult!!
            ) else if (!homeUiState.value.onFilterResult && homeUiState.value.onFilterYear) plantsRepository.getPlantsByYear(
                homeUiState.value.filterYear!!
            ) else plantsRepository.getAllPlants()
                .collect {
                    _plants.value = it
                }
        }
        _homeUiState.update {
            it.copy(
                onFilterCategory = false,
                filterCategory = null,
            )
        }
    }

    fun deleteFilterResult() {
        viewModelScope.launch {
            if (homeUiState.value.onFilterCategory && homeUiState.value.onFilterYear) plantsRepository.getPlantsByCategoryAndYear(
                homeUiState.value.filterCategory!!, homeUiState.value.filterYear!!
            ) else if (homeUiState.value.onFilterCategory && !homeUiState.value.onFilterYear) plantsRepository.getPlantsByCategory(
                homeUiState.value.filterCategory!!
            ) else if (!homeUiState.value.onFilterCategory && homeUiState.value.onFilterYear) plantsRepository.getPlantsByYear(
                homeUiState.value.filterYear!!
            ) else plantsRepository.getAllPlants()
                .collect {
                    _plants.value = it
                }
        }
        _homeUiState.update {
            it.copy(
                onFilterResult = false,
                filterResult = null,
            )
        }
    }

    fun deleteFilterYear() {
        viewModelScope.launch {
            if (homeUiState.value.onFilterCategory && homeUiState.value.onFilterResult) plantsRepository.getPlantsByResultAndCategory(
                homeUiState.value.filterResult!!, homeUiState.value.filterCategory!!
            ) else if (homeUiState.value.onFilterCategory && !homeUiState.value.onFilterResult) plantsRepository.getPlantsByCategory(
                homeUiState.value.filterCategory!!
            ) else if (!homeUiState.value.onFilterCategory && homeUiState.value.onFilterResult) plantsRepository.getPlantsByResult(
                homeUiState.value.filterResult!!
            ) else plantsRepository.getAllPlants()
                .collect {
                    _plants.value = it
                }
        }
        _homeUiState.update {
            it.copy(
                onFilterYear = false,
                filterYear = null,
            )
        }
    }


}

data class HomeUiState(
    val onFilterCategory: Boolean = false,
    val onFilterResult: Boolean = false,
    val onFilterYear: Boolean = false,
    val filterResult: ResultPlant? = null,
    val filterCategory: CategoryPlant? = null,
    val filterYear: String? = null,
    val showBarSearch: Boolean = false
)