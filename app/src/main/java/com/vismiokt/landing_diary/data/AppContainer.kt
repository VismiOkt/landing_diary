package com.vismiokt.landing_diary.data

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore

//private const val USER_PREFERENCES_NAME = "user_preferences"



interface AppContainer {
    val plantsRepository: PlantsRepository
 //   val userPreferencesRepository: UserPreferencesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

//    private val Context.dataStore by preferencesDataStore(
//        name = USER_PREFERENCES_NAME
//    )

    override val plantsRepository: PlantsRepository by lazy {
        OfflinePlantsRepository(LdDatabase.getDatabase(context).plantDao())
    }

//    override val userPreferencesRepository: UserPreferencesRepository by lazy {
//        UserPreferencesRepository(context.dataStore)
//    }

}


