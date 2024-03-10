package com.example.market.data.network_layer

import com.example.market.data.network_layer.models.CategoriesDto
import com.example.market.data.network_layer.models.ProductWrapperDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarketApi {

    @GET("/products/{id}")
    suspend fun getProductById(
        @Path("id") id: String,
    ): ProductWrapperDto.ProductDto

    @GET("/products/categories")
    suspend fun getCategories(): CategoriesDto

    @GET("/products")
    suspend fun getProducts(
        @Query("skip") skip: Int?,
    ): ProductWrapperDto

    @GET("/products/search")
    suspend fun searchProducts(
        @Query("q") q: String?,
        @Query("skip") skip: Int?,
    ): ProductWrapperDto

    @GET("/products/category/{category}")
    suspend fun searchProductsByCategory(
        @Path("category") category: String,
        @Query("skip") skip: Int?,
    ): ProductWrapperDto
}
