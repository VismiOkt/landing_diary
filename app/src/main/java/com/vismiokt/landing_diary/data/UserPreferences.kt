package com.vismiokt.landing_diary.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.vismiokt.landing_diary.R

data class UserPreferences(
    val appTheme: AppTheme,
    val isDynamic: Boolean
) {
}

enum class AppTheme(
    @DrawableRes val image: Int,
    @StringRes val text: Int
) {
    LIGHT(R.drawable.light_theme_small, R.string.settings_theme_light),
    DARK(R.drawable.dark_theme_small, R.string.settings_theme_dark),
    SYSTEM(R.drawable.system_theme_small, R.string.settings_theme_system)
}