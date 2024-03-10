package com.example.market.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberMarketAppState(
    navController: NavHostController = rememberNavController(),
): MarketAppState {
    return remember(navController) {
        MarketAppState(
            navController = navController,
        )
    }
}

@Stable
class MarketAppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    fun onBackClick() {
        navController.popBackStack()
    }
}
