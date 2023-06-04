package com.kenshi.domain.usecase

import com.kenshi.domain.model.PurchaseHistory
import com.kenshi.domain.repository.PurchaseHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PurchaseHistoryUseCase @Inject constructor(
    private val repository: PurchaseHistoryRepository
) {

    fun getPurchaseHistory() : Flow<List<PurchaseHistory>> {
        return repository.getPurchaseHistory()
    }
}