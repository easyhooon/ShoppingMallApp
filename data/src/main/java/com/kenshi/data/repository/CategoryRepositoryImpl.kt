package com.kenshi.data.repository

import com.kenshi.data.datasource.ProductDataSource
import com.kenshi.data.db.dao.LikeDao
import com.kenshi.data.db.entity.toLikeProductEntity
import com.kenshi.domain.model.Category
import com.kenshi.domain.model.Product
import com.kenshi.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dataSource: ProductDataSource,
    private val likeDao: LikeDao
) : CategoryRepository {
    override fun getCategories(): Flow<List<Category>> = flow {
        emit(
            listOf(
                Category.Top, Category.Bag, Category.Outerwear,
                Category.Dress, Category.Pants, Category.FashionAccessories,
                Category.Shoes, Category.Skirt
            )
        )
    }

    override fun getProductByCategory(category: Category): Flow<List<Product>> {
        return dataSource.getHomeComponents().map { list ->
            list.filterIsInstance<Product>().filter { product ->
                product.category.categoryId == category.categoryId
            }
        }
            // filter 가 완료된 후에 combine
            .combine(likeDao.getAll()) { products, likeList ->
                products.map { product ->
                    updateLikeStatus(product, likeList.map { it.productId })
                }
            }
    }

    override suspend fun likeProduct(product: Product) {
        if (product.isLike) {
            likeDao.delete(product.productId)
        } else {
            likeDao.insert(product.toLikeProductEntity().copy(isLike = true))
        }
    }

    private fun updateLikeStatus(product: Product, likeProductIds: List<String>): Product {
        return product.copy(isLike = likeProductIds.contains(product.productId))
    }
}