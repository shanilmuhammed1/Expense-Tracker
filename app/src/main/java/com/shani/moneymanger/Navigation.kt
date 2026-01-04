package com.shani.moneymanger

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val HOME = "home"
    const val CHART = "chart"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            Homescreen(
                onNavigateToChart = { navController.navigate(Routes.CHART) }
            )
        }
        composable(Routes.CHART) {
            ChartScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
