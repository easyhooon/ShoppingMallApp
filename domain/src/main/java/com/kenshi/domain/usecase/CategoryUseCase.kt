package com.kenshi.domain.usecase

import com.kenshi.domain.model.Category
import com.kenshi.domain.model.Product
import com.kenshi.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class  CategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    fun getCategories(): Flow<List<Category>> {
        return categoryRepository.getCategories()
    }

    fun getProductsByCategory(category: Category): Flow<List<Product>> {
        return categoryRepository.getProductByCategory(category)
    }

    suspend fun likeProduct(product: Product) {
        categoryRepository.likeProduct(product)
    }
}