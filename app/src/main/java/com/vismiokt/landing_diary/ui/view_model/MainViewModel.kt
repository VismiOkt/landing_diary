package com.vismiokt.landing_diary.ui.view_model

import androidx.lifecycle.ViewModel
import com.vismiokt.landing_diary.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow


}