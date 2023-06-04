package com.kenshi.domain.usecase

import com.kenshi.domain.model.BasketProduct
import com.kenshi.domain.model.Product
import com.kenshi.domain.repository.BasketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BasketUseCase @Inject constructor(
    private val basketRepository: BasketRepository
) {
    fun getBasketProducts(): Flow<List<BasketProduct>> {
        return basketRepository.getBasketProducts()
    }

    suspend fun deleteBasketProduct(product: Product) {
        basketRepository.deleteBasketProduct(product = product)
    }

    suspend fun checkoutBasket(products: List<BasketProduct>) {
        basketRepository.checkoutBasket(products)
    }
}