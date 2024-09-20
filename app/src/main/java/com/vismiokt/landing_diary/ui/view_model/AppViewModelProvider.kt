package com.vismiokt.landing_diary.ui.view_model

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vismiokt.landing_diary.LdApplication

object AppViewModelProvider {
    @RequiresApi(Build.VERSION_CODES.O)
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                ldApplication().container.plantsRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            PlantEntryViewModel(ldApplication().container.plantsRepository)
        }

        initializer {
            PlantCardViewModel(
                ldApplication().container.plantsRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            PlantEditViewModel(
                ldApplication().container.plantsRepository,
                this.createSavedStateHandle()
            )
        }
    }

}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.ldApplication(): LdApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LdApplication)
