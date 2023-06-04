package com.kenshi.data.repository

import com.kenshi.domain.model.PurchaseHistory
import com.kenshi.domain.repository.PurchaseHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PurchaseHistoryRepositoryImpl @Inject constructor(
    private val repository: PurchaseHistoryRepository
) {

    fun getPurchaseHistory(): Flow<List<PurchaseHistory>> {
        return repository.getPurchaseHistory()
    }
}