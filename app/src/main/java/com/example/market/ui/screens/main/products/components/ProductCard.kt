package com.example.market.ui.screens.main.products.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Percent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.market.domain.models.Product
import com.example.market.ui.components.BucketButton
import com.example.market.ui.theme.MarketTheme
import com.example.market.utils.shimmerBrush
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ProductCard(
    modifier: Modifier,
    product: Product,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        elevation = 6.dp,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Box(modifier = Modifier) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    model = product.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.small)
                                .aspectRatio(1f)
                                .background(brush = shimmerBrush())
                        )
                    }
                )
                product.discountPercentage?.let { discountPercentage ->
                    DiscountBadge(
                        modifier = Modifier.padding(8.dp),
                        discountPercentage = discountPercentage.roundToInt()
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                ProductPrice(
                    price = product.price ?: 0,
                    discountPercentage = product.discountPercentage?.roundToInt(),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier
                        .basicMarquee(),
                    text = product.title.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(4.dp))
                ProductChipRow(
                    modifier = Modifier,
                    product = product,
                )
                BucketButton(
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductChipRow(
    modifier: Modifier = Modifier,
    product: Product,
) {
    LazyRow(
        modifier = modifier
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (product.discountPercentage != null) {
            item {
                Chip(
                    onClick = {},
                    enabled = false,
                    colors = ChipDefaults.chipColors(
                        backgroundColor = Color.Red.copy(.1f),
                        disabledBackgroundColor = Color.Red.copy(.1f),
                    ),
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(1.dp, color = Color.Red)
                ) {
                    Text(
                        text = "Скидка",
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
        if ((product.stock ?: 0) < 10) {
            item {
                Chip(
                    onClick = {},
                    enabled = false,
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(1.dp, color = LightGray),
                    colors = ChipDefaults.chipColors(
                        backgroundColor = LightGray.copy(.1f),
                        disabledBackgroundColor = LightGray.copy(.1f),
                    )
                ) {
                    Text(
                        text = "Осталось мало",
                        style = MaterialTheme.typography.labelMedium,
                        color = LightGray
                    )
                }
            }
        }
        if ((product.rating ?: 0.0) > 4.7) {
            item {
                Chip(
                    onClick = {},
                    enabled = false,
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(1.dp, color = Yellow),
                    colors = ChipDefaults.chipColors(
                        backgroundColor = Yellow.copy(.1f),
                        disabledBackgroundColor = Yellow.copy(.1f),
                    )
                ) {
                    Text(
                        text = "Лучшее",
                        style = MaterialTheme.typography.labelMedium,
                        color = Yellow
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDiscountBadge() {
    MarketTheme {
        DiscountBadge(
            modifier = Modifier,
            discountPercentage = 12
        )
    }
}

@Composable
fun DiscountBadge(
    modifier: Modifier = Modifier,
    discountPercentage: Int,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(Color(0xFFFFBDBB))
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = discountPercentage.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFFCA0021),
            fontWeight = FontWeight.Bold,
        )
        Icon(
            modifier = Modifier
                .size(12.dp),
            imageVector = Icons.Rounded.Percent,
            contentDescription = null,
            tint = Color(0xFFCA0021)
        )
    }
}
