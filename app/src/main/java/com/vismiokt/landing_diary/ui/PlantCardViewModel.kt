package com.vismiokt.landing_diary.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vismiokt.landing_diary.data.PlantsRepository
import com.vismiokt.landing_diary.domain.FormatDateUseCase
import com.vismiokt.landing_diary.navigation.Screen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PlantCardViewModel(
    private val repository: PlantsRepository,
    savedStateHandle: SavedStateHandle
    ) : ViewModel() {

    private val plantId: Int = checkNotNull(savedStateHandle[Screen.PlantCardDestination.plantId])
    val uiState: StateFlow<PlantCardUiState> =
        repository.getPlantStream(plantId)
            .filterNotNull()
            .map {
                PlantCardUiState(plantDetails = it.toPlantDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = PlantCardUiState()
            )


}

data class PlantCardUiState (
    val plantDetails: PlantDetails = PlantDetails()
)