package com.kenshi.domain.repository

import com.kenshi.domain.model.Category
import com.kenshi.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>

    fun getProductByCategory(category: Category): Flow<List<Product>>

    suspend fun likeProduct(product: Product)
}