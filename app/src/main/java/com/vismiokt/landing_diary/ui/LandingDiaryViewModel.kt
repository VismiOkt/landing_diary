package com.vismiokt.landing_diary.ui

import androidx.lifecycle.ViewModel
import com.vismiokt.landing_diary.data.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LandingDiaryViewModel : ViewModel() {
    val plants: List<Plant> = listOf(Plant(1, "tutu"))

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun closeDatePickerDialog() {
        _uiState.update {
            it.copy(openDialogCalendar = false)
        }
    }

    fun openDatePickerDialog() {
        _uiState.update {
            it.copy(openDialogCalendar = true)
        }
    }



}