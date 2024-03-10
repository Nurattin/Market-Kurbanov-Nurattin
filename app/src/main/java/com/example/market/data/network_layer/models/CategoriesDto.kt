package com.example.market.data.network_layer.models

import com.example.market.domain.models.Category
import kotlinx.serialization.Serializable


@Serializable
class CategoriesDto : ArrayList<String>()

fun CategoriesDto.toCategories() = map {
    Category(
        name = it
    )
}