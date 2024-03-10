package com.example.market.ui.screens.main.products.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.market.data.handleException
import com.example.market.domain.models.Product
import com.example.market.ui.components.MarketEmptyList
import com.example.market.ui.components.MarketErrorScreen
import com.example.market.ui.components.MarketLoadingScreen
import com.example.market.ui.screens.main.products.ProductEvent

@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    products: LazyPagingItems<Product>,
    onEvent: (ProductEvent) -> Unit
) {
    if (products.loadState.refresh is LoadState.Loading) {
        MarketLoadingScreen(
            modifier = modifier
                .fillMaxSize()
        )
    } else if (products.loadState.refresh is LoadState.Error) {
        MarketErrorScreen(
            modifier = modifier
                .fillMaxSize(),
            errorText = handleException((products.loadState.refresh as? LoadState.Error)?.error),
            onClickRetry = products::refresh,
        )
    } else if (products.loadState.refresh is LoadState.NotLoading) {
        if (products.itemCount == 0) {
            MarketEmptyList(
                modifier = modifier
                    .fillMaxSize()
            )
        } else {
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(15.dp),
                columns = GridCells.Fixed(2),
            ) {
                when (products.loadState.refresh) {
                    is LoadState.NotLoading -> {
                        if (products.itemCount != 0) {
                            items(
                                count = products.itemCount,
                            ) { index ->
                                products[index]?.let { product ->
                                    ProductCard(
                                        modifier = Modifier,
                                        product = product,
                                        onClick = {
                                            onEvent(ProductEvent.SelectedProduct(product.id.toString()))
                                        }
                                    )
                                }
                            }
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}
