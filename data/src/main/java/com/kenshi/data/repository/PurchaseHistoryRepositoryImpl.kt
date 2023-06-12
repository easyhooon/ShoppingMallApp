package com.kenshi.data.repository

import com.kenshi.data.db.dao.PurchaseHistoryDao
import com.kenshi.data.db.entity.toDomainModel
import com.kenshi.domain.model.PurchaseHistory
import com.kenshi.domain.repository.PurchaseHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PurchaseHistoryRepositoryImpl @Inject constructor(
    private val purchaseHistoryDao: PurchaseHistoryDao
) : PurchaseHistoryRepository {

    override fun getPurchaseHistory(): Flow<List<PurchaseHistory>> {
        return purchaseHistoryDao.getAll().map { list ->
            list.map { it.toDomainModel() }
        }
    }
}