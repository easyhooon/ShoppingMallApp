package com.kenshi.domain.repository

import com.kenshi.domain.model.BasketProduct
import com.kenshi.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface BasketRepository {
    fun getBasketProducts(): Flow<List<BasketProduct>>

    suspend fun deleteBasketProduct(product: Product)

    // 결제 기능
    suspend fun checkoutBasket(products: List<BasketProduct>)
}