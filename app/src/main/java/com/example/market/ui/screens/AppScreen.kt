package com.example.market.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.market.ui.screens.navigation.MarketNavHost

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppScreen(
    modifier: Modifier = Modifier,
    appState: MarketAppState = rememberMarketAppState()
) {

    Scaffold(
        modifier = modifier
            .navigationBarsPadding(),
        containerColor = Color.White,
    ) {
        MarketNavHost(
            modifier = Modifier,
            navController = appState.navController,
        )
    }
}