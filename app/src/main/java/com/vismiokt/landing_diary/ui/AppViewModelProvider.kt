package com.vismiokt.landing_diary.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.vismiokt.landing_diary.LdApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            LandingDiaryViewModel()
        }
        // Initializer for ItemEntryViewModel
//        initializer {
//            ItemEntryViewModel()
//        }
//
//        // Initializer for ItemDetailsViewModel
//        initializer {
//            ItemDetailsViewModel(
//                this.createSavedStateHandle()
//            )
//        }
//
//        // Initializer for HomeViewModel
//        initializer {
//            HomeViewModel()
//        }
//    }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.inventoryApplication(): LdApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LdApplication)
