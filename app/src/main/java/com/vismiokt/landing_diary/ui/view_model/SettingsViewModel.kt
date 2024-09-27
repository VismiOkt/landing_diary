package com.vismiokt.landing_diary.ui.view_model

import android.graphics.drawable.Icon
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel: ViewModel() {

    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    init {

    }



}

data class SettingsUiState(
    val onDynamicTheme: Boolean = false,
    val onDarkTheme: Boolean = false,
    val onSystemTheme: Boolean? = null,
    val supportDynamicTheme: Boolean = false
)

