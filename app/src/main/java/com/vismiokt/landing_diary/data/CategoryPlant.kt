package com.vismiokt.landing_diary.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.vismiokt.landing_diary.R

enum class CategoryPlant (@StringRes val title: Int, @DrawableRes val icon: Int) {
    vegetables(R.string.category_vegetables, R.drawable.icon_vegetables),
    herbs(R.string.category_herbs, R.drawable.icon_herbs),
    flowers(R.string.category_flowers, R.drawable.icon_flowers),
    berries(R.string.category_berries, R.drawable.icon_berries),
    seedlings(R.string.category_seedlings, R.drawable.icon_seedlings),
    mushrooms(R.string.category_mushrooms, R.drawable.icon_mushrooms),
    exotic(R.string.category_exotic, R.drawable.icon_exotic),
    other(R.string.category_other, R.drawable.icon_other)
}