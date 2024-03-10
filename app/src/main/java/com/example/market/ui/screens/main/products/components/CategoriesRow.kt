package com.example.market.ui.screens.main.products.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.market.domain.models.Category

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
fun CategoriesRow(
    categories: List<Category>,
    selectedCategory: Category?,
    onClick: (Category) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 4.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = categories
        ) { category ->
            FilterChip(
                selected = category == selectedCategory,
                shape = MaterialTheme.shapes.small,
                onClick = { onClick(category) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                ),
                label = {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            )
        }
    }
}