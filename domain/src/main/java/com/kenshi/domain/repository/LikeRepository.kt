package com.kenshi.domain.repository

import com.kenshi.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface LikeRepository {

    fun getLikeProduct(): Flow<List<Product>>
}