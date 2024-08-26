package com.vismiokt.landing_diary.data

import androidx.annotation.StringRes
import com.vismiokt.landing_diary.R

enum class FilterPlant (@StringRes val title: Int) {
    year (R.string.plant_filter_year),
    category (R.string.plant_filter_category),
    result (R.string.plant_filter_result)
}