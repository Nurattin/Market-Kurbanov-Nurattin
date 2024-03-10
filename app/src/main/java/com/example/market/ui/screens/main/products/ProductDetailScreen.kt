package com.example.market.ui.screens.main.products

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.market.domain.models.Product
import com.example.market.ui.components.BucketButton
import com.example.market.ui.components.MarketErrorScreen
import com.example.market.ui.components.MarketLoadingScreen
import com.example.market.ui.screens.main.products.components.DiscountBadge
import com.example.market.ui.screens.main.products.components.ProductPrice
import com.example.market.utils.shimmerBrush
import kotlin.math.roundToInt


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    product: Product,
    loading: Boolean,
    error: String?,
    onClickBack: () -> Unit,
    onClickRetry: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val isDarkTheme = isSystemInDarkTheme()

    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState {
        product.images.orEmpty().size
    }

    DisposableEffect(focusManager) {
        focusManager.clearFocus(true)
        onDispose { }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                TopAppBar(
                    windowInsets = WindowInsets.statusBars,
                    navigationIcon = {
                        IconButton(onClick = onClickBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {}
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    product.discountPercentage?.let { discountPercentage ->
                        DiscountBadge(
                            modifier = Modifier,
                            discountPercentage = discountPercentage.roundToInt()
                        )
                    }
                    ProductPrice(
                        price = product.price ?: 0,
                        discountPercentage = product.discountPercentage?.roundToInt(),
                    )
                }
                BucketButton(
                    modifier = Modifier
                        .width(120.dp),
                )
            }
        }
    ) {
        if (!error.isNullOrBlank()) {
            MarketErrorScreen(
                errorText = error,
                onClickRetry = onClickRetry,
            )
        } else if (loading) {
            MarketLoadingScreen()
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
            ) {
                Box(modifier = Modifier) {
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp),
                        state = pagerState,
                    ) { page ->
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxSize(),
                            model = product.images.orEmpty()[page],
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(brush = shimmerBrush())
                                )
                            },
                        )
                    }
                    if (!product.images.isNullOrEmpty()) {
                        PageIndicators(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            numberOfPages = pagerState.pageCount,
                            selectedPage = pagerState.currentPage,
                            defaultRadius = 6.dp,
                            selectedColor = MaterialTheme.colorScheme.primary,
                            defaultColor = Color.Gray,
                            space = 2.5.dp,
                            selectedLength = 12.dp,
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 32.dp,
                            vertical = 20.dp
                        )
                ) {
                    Text(
                        text = product.title.orEmpty(),
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = product.description.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isDarkTheme) Color.LightGray else Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp + it.calculateBottomPadding()))
                }
            }
        }
    }
}

@Composable
fun PageIndicators(
    numberOfPages: Int,
    modifier: Modifier = Modifier,
    selectedPage: Int = 0,
    selectedColor: Color = Color(0xFF3E6383),
    defaultColor: Color = Color.LightGray,
    defaultRadius: Dp = 20.dp,
    selectedLength: Dp = 60.dp,
    space: Dp = 30.dp,
    animationDurationInMillis: Int = 300,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space),
        modifier = modifier,
    ) {
        for (i in 0 until numberOfPages) {
            val isSelected = i == selectedPage
            PageIndicator(
                isSelected = isSelected,
                selectedColor = selectedColor,
                defaultColor = defaultColor,
                defaultRadius = defaultRadius,
                selectedLength = selectedLength,
                animationDurationInMillis = animationDurationInMillis,
            )
        }
    }
}

@Composable
fun PageIndicator(
    isSelected: Boolean,
    selectedColor: Color,
    defaultColor: Color,
    defaultRadius: Dp,
    selectedLength: Dp,
    animationDurationInMillis: Int,
    modifier: Modifier = Modifier,
) {
    val color: Color by animateColorAsState(
        targetValue = if (isSelected) {
            selectedColor
        } else {
            defaultColor
        },
        animationSpec = tween(
            durationMillis = animationDurationInMillis,
        )
    )
    val width: Dp by animateDpAsState(
        targetValue = if (isSelected) {
            selectedLength
        } else {
            defaultRadius
        },
        animationSpec = tween(
            durationMillis = 1500,
            easing = EaseOutElastic
        )
    )

    Canvas(
        modifier = modifier
            .size(
                width = width,
                height = defaultRadius,
            ),
    ) {
        drawRoundRect(
            color = color,
            topLeft = Offset.Zero,
            size = Size(
                width = width.toPx(),
                height = defaultRadius.toPx(),
            ),
            cornerRadius = CornerRadius(
                x = defaultRadius.toPx(),
                y = defaultRadius.toPx(),
            ),
        )
    }
}
