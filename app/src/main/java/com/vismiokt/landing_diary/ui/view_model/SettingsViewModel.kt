package com.vismiokt.landing_diary.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vismiokt.landing_diary.data.AppTheme
import com.vismiokt.landing_diary.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

//    private val _settingsUiState = MutableStateFlow(SettingsUiState())
//    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState = _settingsUiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesFlow.map { preferences ->
                return@map SettingsUiState(
                    isDynamicTheme = preferences.isDynamic,
                    appTheme = preferences.appTheme
                )
            }.collect {
                _settingsUiState.value = it
            }
        }


    }


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
    val appTheme: AppTheme = AppTheme.SYSTEM,
//    val supportDynamicTheme: Boolean = false
)

