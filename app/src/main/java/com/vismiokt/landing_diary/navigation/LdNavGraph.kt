package com.vismiokt.landing_diary.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vismiokt.landing_diary.ui.HomeScreen
import com.vismiokt.landing_diary.ui.PlantCard
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
                navigateToPlantEntry = { navController.navigate(Screen.PlantEntryDestination.route) }
                )
        }
        composable(route = Screen.PlantCardDestination.route) {
            PlantCard(drawerState)
        }
        composable(route = Screen.PlantEntryDestination.route) {
            PlantEntryScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
    }



}