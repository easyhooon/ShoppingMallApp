package com.kenshi.data.repository

import com.kenshi.data.datasource.ProductDataSource
import com.kenshi.data.db.dao.BasketDao
import com.kenshi.data.db.entity.toBasketProductEntity
import com.kenshi.domain.model.Product
import com.kenshi.domain.repository.ProductDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductDetailRepositoryImpl @Inject constructor(
    private val dataSource: ProductDataSource,
    private val basketDao: BasketDao,
) : ProductDetailRepository {
    override fun getProductDetail(productId: String): Flow<Product> {
        return dataSource.getHomeComponents().map { products ->
            products.filterIsInstance<Product>().first { it.productId == productId }
        }
    }

    // 실무에서는 이런 부분은 서버에서 처리하게 될 내용
    override suspend fun addBasket(product: Product) {
        val alreadyExistProduct = basketDao.get(product.productId)
        if (alreadyExistProduct == null) {
            basketDao.insert(product.toBasketProductEntity())
        } else {
            basketDao.insert(alreadyExistProduct.copy(count = alreadyExistProduct.count + 1))
        }
    }
}