package com.example.market.ui.screens.main.products

import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.market.utils.ObserveEffect

@Composable
fun ProductsRoute(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val viewModel: ProductViewModel = hiltViewModel()
    val uiState by viewModel.productUiState.collectAsStateWithLifecycle()

    ObserveEffect(viewModel.effectFlow) { effect ->
        when (effect) {
            is ProductEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    BackHandler(uiState.selectedProduct != null && configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        viewModel.obtainEvent(
            ProductEvent.ClearSelectedProduct
        )
    }

    val productList = viewModel.productList.collectAsLazyPagingItems()

    ProductsScreen(
        modifier = modifier,
        uiState = uiState,
        productList = productList,
        onEvent = viewModel::obtainEvent,
    )

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        uiState.selectedProduct?.let { selectedProduct ->
            ProductDetailScreen(
                modifier = Modifier,
                product = selectedProduct,
                error = uiState.errorFilmDetail,
                loading = uiState.productDetailLoading,
                onClickBack = {
                    viewModel.obtainEvent(
                        ProductEvent.ClearSelectedProduct
                    )
                },
                onClickRetry = {
                    viewModel.obtainEvent(ProductEvent.RefreshProductDetail(selectedProduct.id.toString()))
                }
            )
        }
    }
}