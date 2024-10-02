package com.vismiokt.landing_diary.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vismiokt.landing_diary.data.AppTheme
import com.vismiokt.landing_diary.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

//    private val _settingsUiState = MutableStateFlow(SettingsUiState())
//    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow



    private val _settingsUiState = userPreferencesFlow.map {
        return@map SettingsUiState(
            isDynamicTheme = it.isDynamic,
            appTheme = it.appTheme
        )
    }

    val settingsUiState = _settingsUiState.asLiveData()

    fun onDynamicTheme(enable: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateIsDynamic(enable)
        }
    }

    fun selectAppTheme(appTheme: AppTheme) {
        viewModelScope.launch {
            userPreferencesRepository.updateAppTheme(appTheme)
        }
    }


}

data class SettingsUiState(
    val isDynamicTheme: Boolean = false,
    val appTheme: AppTheme = AppTheme.SYSTEM
//    val onDarkTheme: Boolean = false,
//    val onSystemTheme: Boolean? = null,
//    val supportDynamicTheme: Boolean = false
)

