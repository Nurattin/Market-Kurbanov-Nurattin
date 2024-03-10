package com.example.market.domain.repository

import androidx.paging.PagingData
import com.example.market.domain.models.Category
import com.example.market.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getProductDetail(
        id: String,
    ): Flow<Product>

    fun getProduct(
        query: String?,
        category: String?,
    ): Flow<PagingData<Product>>

    fun getCategories(): Flow<List<Category>>
}