package com.vismiokt.landing_diary.data

import android.content.Context

interface AppContainer {
    val plantsRepository: PlantsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val plantsRepository: PlantsRepository by lazy {
        OfflinePlantsRepository(LdDatabase.getDatabase(context).plantDao())
    }

}


