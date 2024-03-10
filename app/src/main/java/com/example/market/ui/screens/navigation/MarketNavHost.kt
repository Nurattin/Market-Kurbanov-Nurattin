package com.example.market.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.market.ui.screens.main.products.products
import com.example.market.ui.screens.main.navigation.MAIN_ROUTE_PATTERN
import com.example.market.ui.screens.main.navigation.main

@Composable
fun MarketNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val startDestination = MAIN_ROUTE_PATTERN

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        main {
            products()
        }
    }
}
