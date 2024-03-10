package com.example.market.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.market.data.network_layer.MarketApi
import com.example.market.data.network_layer.models.ProductWrapperDto
import com.example.market.data.network_layer.models.toProduct
import com.example.market.domain.models.Product

class ProductPagingSource(
    private val api: MarketApi,
    private val query: String?,
    private val category: String?,
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {

            val cursor = params.key

            val repo = if (category != null) {
                api.searchProductsByCategory(
                    skip = cursor,
                    category = category,
                )
            } else if (query.isNullOrBlank()) {
                api.getProducts(
                    skip = cursor,
                )
            } else {
                api.searchProducts(
                    q = query,
                    skip = cursor,
                )
            }

            val products = repo.products?.map(ProductWrapperDto.ProductDto::toProduct).orEmpty()

            LoadResult.Page(
                data = products,
                prevKey = if (repo.skip == 0) null else repo.skip - 1,
                nextKey = if (repo.skip == repo.total) null else repo.skip + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey ?: page?.nextKey
        }
    }
}