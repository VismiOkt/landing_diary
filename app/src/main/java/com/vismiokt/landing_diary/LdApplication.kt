package com.vismiokt.landing_diary

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.vismiokt.landing_diary.data.AppContainer
import com.vismiokt.landing_diary.data.AppDataContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LdApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}