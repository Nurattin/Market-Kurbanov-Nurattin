package com.example.market.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.market.data.network_layer.MarketApi
import com.example.market.data.network_layer.models.toCategories
import com.example.market.data.network_layer.models.toProduct
import com.example.market.di.IoDispatcher
import com.example.market.domain.models.Product
import com.example.market.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: MarketApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ProductRepository {
    override fun getProduct(
        query: String?,
        category: String?,
    ): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                ProductPagingSource(
                    api = api,
                    query = query,
                    category = category,
                )
            }
        )
            .flow
            .flowOn(ioDispatcher)
    }

    override fun getCategories() = flow {
        val categoriesDto = api.getCategories()
        val categories = categoriesDto.toCategories()

        emit(categories)
    }

    override fun getProductDetail(id: String): Flow<Product> = flow {
        val filmDetailDto = api.getProductById(id)
        val filmDetail = filmDetailDto.toProduct()

        emit(filmDetail)
    }.flowOn(ioDispatcher)
}
