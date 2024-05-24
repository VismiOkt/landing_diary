package com.vismiokt.landing_diary.data

import androidx.annotation.StringRes
import com.vismiokt.landing_diary.R

enum class CategoryPlant (@StringRes val title: Int) {
    vegetables(R.string.category_vegetables),
    herbs(R.string.category_herbs),
    flowers(R.string.category_flowers),
    berries(R.string.category_berries),
    seedlings(R.string.category_seedlings),
    mushrooms(R.string.category_mushrooms),
    exotic(R.string.category_exotic)
}