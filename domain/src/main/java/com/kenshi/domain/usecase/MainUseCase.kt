package com.kenshi.domain.usecase

import com.kenshi.domain.model.BaseModel
import com.kenshi.domain.model.Product
import com.kenshi.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    fun getModelList(): Flow<List<BaseModel>> {
        return mainRepository.getModelList()
    }

    suspend fun likeProduct(product: Product) {
        mainRepository.likeProduct(product)
    }
}