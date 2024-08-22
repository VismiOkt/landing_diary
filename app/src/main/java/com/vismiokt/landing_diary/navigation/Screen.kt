package com.vismiokt.landing_diary.navigation

sealed class Screen (
    val route: String,
 //   @StringRes val titleRes: Int
) {
    data object HomeDestination : Screen(ROUTE_HOME)
    data object PlantCardDestination : Screen(ROUTE_PLANT_CARD) {
        const val plantId = "plantId"
        val routeFull = "$ROUTE_PLANT_CARD/{$plantId}"
    }

    data object PlantEntryDestination : Screen(ROUTE_PLANT_ENTRY)

    data object PlantEditDestination : Screen(ROUTE_PLANT_EDIT) {
        const val plantId = "plantId"
        val routeFull = "$ROUTE_PLANT_EDIT/{${plantId}}"
    }

    data object RequiredPermissionDestination : Screen(ROUT_REQUIRED_PERMISSION)

    companion object {
        const val ROUTE_HOME: String = "home"
        const val ROUTE_PLANT_CARD: String = "plant_card"
        const val ROUTE_PLANT_ENTRY: String = "plant_entry"
        const val ROUTE_PLANT_EDIT: String = "plant_edit"
        const val ROUT_REQUIRED_PERMISSION: String = "required_permission"
    }


}