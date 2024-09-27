package com.vismiokt.landing_diary.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vismiokt.landing_diary.ui.AboutAppScreen
import com.vismiokt.landing_diary.ui.HomeScreen
import com.vismiokt.landing_diary.ui.PlantCard
import com.vismiokt.landing_diary.ui.PlantEditScreen
import com.vismiokt.landing_diary.ui.PlantEntryScreen
import com.vismiokt.landing_diary.ui.SettingsScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LdNavigation(
    navController: NavHostController,
    drawerState: DrawerState,


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
                navigateBack = { navController.navigateUp() }
                //{ navController.navigate(Screen.HomeDestination.route) }
            )
        }
        composable(route = Screen.PlantEntryDestination.route) {
            PlantEntryScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(route = Screen.PlantEditDestination.routeFull,
            arguments = listOf(navArgument(Screen.PlantEditDestination.plantId) {
                type = NavType.IntType
            })
        ) {
            PlantEditScreen(
                navigateBack = { navController.navigateUp() },
                navigateToPlantDetails = { navController.navigateUp() }
            //    { navController.navigate("${Screen.PlantCardDestination.route}/${it}") }
            )
        }
        composable(route = Screen.AboutAppDestination.route) {
            AboutAppScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(route = Screen.SettingsDestination.route) {
            SettingsScreen(
                navigateBack = { navController.navigateUp() }
            )
        }

    }


}