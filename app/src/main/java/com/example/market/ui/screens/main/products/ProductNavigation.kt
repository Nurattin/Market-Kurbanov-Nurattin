package com.example.market.ui.screens.main.products

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.market.ui.screens.main.navigation.mainNavigationRoute


fun NavGraphBuilder.products(
) {
    composable(
        route = mainNavigationRoute,
    ) {
        ProductsRoute()
    }
}
