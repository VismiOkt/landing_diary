package com.vismiokt.landing_diary.data

data class UserPreferences(
    val appTheme: AppTheme,
    val isDynamic: Boolean
) {
}

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM
}