package com.vismiokt.landing_diary

import android.app.Application
import com.vismiokt.landing_diary.data.AppContainer
import com.vismiokt.landing_diary.data.AppDataContainer

class LdApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}