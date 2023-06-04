package com.kenshi.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.kenshi.data.db.dao.BasketDao
import com.kenshi.data.db.dao.PurchaseHistoryDao
import com.kenshi.data.db.entity.PurchaseHistoryEntity
import com.kenshi.data.db.entity.toDomainModel
import com.kenshi.domain.model.BasketProduct
import com.kenshi.domain.model.Product
import com.kenshi.domain.repository.BasketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime
import javax.inject.Inject

class BasketRepositoryImpl @Inject constructor(
    private val basketDao: BasketDao,
    private val purchaseHistoryDao: PurchaseHistoryDao
) : BasketRepository {
    override fun getBasketProducts(): Flow<List<BasketProduct>> {
        return basketDao.getAll().map { list ->
            list.map { BasketProduct(it.toDomainModel(), it.count) }
        }
    }

    override suspend fun deleteBasketProduct(product: Product) {
        return basketDao.delete(product.productId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun checkoutBasket(products: List<BasketProduct>) {
        purchaseHistoryDao.insert(PurchaseHistoryEntity(products, ZonedDateTime.now()))
        basketDao.deleteAll()
    }
}