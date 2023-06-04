package com.kenshi.domain.usecase

import com.kenshi.domain.model.Product
import com.kenshi.domain.repository.LikeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LikeUseCase @Inject constructor(
    private val repository: LikeRepository
) {
    fun getLikeProducts(): Flow<List<Product>> {
        return repository.getLikeProduct()
    }
}