package com.vismiokt.landing_diary.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vismiokt.landing_diary.ui.RequiredPermission
import com.vismiokt.landing_diary.ui.HomeScreen
import com.vismiokt.landing_diary.ui.PlantCard
import com.vismiokt.landing_diary.ui.PlantEditScreen
import com.vismiokt.landing_diary.ui.PlantEntryScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LdNavigation(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState

) {
    NavHost(navController = navController, startDestination = Screen.HomeDestination.route) {
        composable(route = Screen.HomeDestination.route) {
            HomeScreen(
                drawerState,
                navigateToPlantEntry = { navController.navigate(Screen.PlantEntryDestination.route) },
                navigateToPlantDetails = { navController.navigate("${Screen.PlantCardDestination.route}/${it}") }
            )
        }
        composable(
            route = Screen.PlantCardDestination.routeFull,
            arguments = listOf(navArgument(Screen.PlantCardDestination.plantId) {
                type = NavType.IntType
            })
        ) {
            PlantCard(
                navigateToEditPlant = { navController.navigate("${Screen.PlantEditDestination.route}/${it}") },
                navigateBack = { navController.navigate(Screen.HomeDestination.route) }
            )
        }
        composable(route = Screen.PlantEntryDestination.route) {
            PlantEntryScreen(
                navigateBack = { navController.navigateUp() },
                navigateCamera = { navController.navigate(Screen.RequiredPermissionDestination.route) },

            )
        }
        composable(route = Screen.PlantEditDestination.routeFull,
            arguments = listOf(navArgument(Screen.PlantEditDestination.plantId) {
                type = NavType.IntType
            })
        ) {
            PlantEditScreen(
                navigateBack = { navController.navigateUp() },
                navigateCamera = { navController.navigate(Screen.RequiredPermissionDestination.route) },
                navigateToPlantDetails = { navController.navigate("${Screen.PlantCardDestination.route}/${it}") }
            )
        }
        composable(route = Screen.RequiredPermissionDestination.route) {
            RequiredPermission(
                navigateBack = { navController.navigateUp() },
            )
        }
    }


}