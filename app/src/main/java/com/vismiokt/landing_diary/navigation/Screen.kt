package com.vismiokt.landing_diary.navigation

import androidx.annotation.StringRes

sealed class Screen (
    val route: String,
 //   @StringRes val titleRes: Int
) {
    data object HomeDestination : Screen(ROUTE_HOME)
    data object PlantCardDestination : Screen(ROUTE_PLANT_CARD)

    data object PlantEntryDestination : Screen(ROUTE_PLANT_ENTRY)

    companion object {
        const val ROUTE_HOME: String = "home"
        const val ROUTE_PLANT_CARD: String = "plant_card"
        const val ROUTE_PLANT_ENTRY: String = "plant_entry"
    }


}