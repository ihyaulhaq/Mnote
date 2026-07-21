package com.github.ihyaulhaq.mnote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.ihyaulhaq.mnote.ui.home.HomeScreen
import com.github.ihyaulhaq.mnote.ui.stats.StatsScreen

object Routes {
    const val HOME = "home"
    const val STATS = "stats"
}

@Composable
fun MnoteNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToStats = { navController.navigate(Routes.STATS) }
            )
        }
        composable(Routes.STATS) {
            StatsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
