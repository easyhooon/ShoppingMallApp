package com.kenshi.domain.repository

import com.kenshi.domain.model.BaseModel
import com.kenshi.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getModelList(): Flow<List<BaseModel>>

    suspend fun likeProduct(product: Product)
}