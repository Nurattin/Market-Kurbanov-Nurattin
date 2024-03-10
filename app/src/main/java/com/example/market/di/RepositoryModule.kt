package com.example.market.di

import com.example.market.data.repository.ProductRepositoryImpl
import com.example.market.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindProductRepository(repo: ProductRepositoryImpl): ProductRepository
}
