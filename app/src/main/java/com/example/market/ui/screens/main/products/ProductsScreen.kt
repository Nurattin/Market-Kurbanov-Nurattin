package com.example.market.ui.screens.main.products

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.market.R
import com.example.market.domain.models.Product
import com.example.market.ui.screens.main.products.components.CategoriesRow
import com.example.market.ui.screens.main.products.components.MarketSearchBar
import com.example.market.ui.screens.main.products.components.ProductList

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    uiState: FilmsUiState,
    onEvent: (ProductEvent) -> Unit,
    productList: LazyPagingItems<Product>,
) {
    val configuration = LocalConfiguration.current

    var searchBarIsActive by rememberSaveable {
        mutableStateOf(false)
    }

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1f else .5f),
                backgroundColor = MaterialTheme.colorScheme.surface,
                topBar = {
                    if (searchBarIsActive) {
                        MarketSearchBar(
                            modifier = Modifier
                                .statusBarsPadding(),
                            value = uiState.searchBarText,
                            enabled = uiState.selectedCategory == null,
                            onValueChange = { newValue ->
                                onEvent(
                                    ProductEvent.ChangeSearchBarText(newValue)
                                )
                            },
                            onClickBack = {
                                searchBarIsActive = false
                            },
                        )
                    } else {
                        TopAppBar(
                            windowInsets = WindowInsets.statusBars,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            backgroundColor = MaterialTheme.colorScheme.background,
                            elevation = 0.dp,
                            title = {
                                Text(
                                    text = stringResource(R.string.popular),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        searchBarIsActive = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Search,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                        )
                    }
                },
            ) { innerPadding ->
                Box(modifier = Modifier) {
                    ProductList(
                        modifier = Modifier
                            .padding(innerPadding),
                        products = productList,
                        onEvent = onEvent,
                    )
                    CategoriesRow(
                        categories = uiState.categories,
                        selectedCategory = uiState.selectedCategory,
                        onClick = { category ->
                            onEvent(ProductEvent.SelectedCategory(category))
                        }
                    )
                }

            }
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                uiState.selectedProduct?.let { selectedProduct ->
                    ProductDetailScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        product = selectedProduct,
                        error = uiState.errorFilmDetail,
                        loading = uiState.productDetailLoading,
                        onClickBack = {
                            onEvent(ProductEvent.ClearSelectedProduct)
                        },
                        onClickRetry = {
                            onEvent(ProductEvent.RefreshProductDetail(selectedProduct.id.toString()))
                        }
                    )
                }
            }
        }
    }
}