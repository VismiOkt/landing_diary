package com.vismiokt.landing_diary.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

interface UserPreferencesRepository {

    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateIsDynamic(isDynamic: Boolean)

    suspend fun updateAppTheme(appTheme: AppTheme)

}

class MyUserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
    ): UserPreferencesRepository {

    private object PreferencesKeys {
        val IS_DYNAMIC = booleanPreferencesKey("is_dynamic")
        val APP_THEME = stringPreferencesKey("app_theme")
    }

    override val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val isDynamic = preferences[PreferencesKeys.IS_DYNAMIC] ?: false

            val appTheme =
                AppTheme.valueOf(
                    preferences[PreferencesKeys.APP_THEME] ?: AppTheme.SYSTEM.name)

            UserPreferences(appTheme, isDynamic)

        }

    override suspend fun updateIsDynamic(isDynamic: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DYNAMIC] = isDynamic
        }
    }

    override suspend fun updateAppTheme(appTheme: AppTheme) {
        dataStore.edit { preferences ->
           preferences[PreferencesKeys.APP_THEME] = appTheme.name
        }
    }



}