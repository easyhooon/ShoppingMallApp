package com.kenshi.data.repository

import com.kenshi.data.db.dao.LikeDao
import com.kenshi.data.db.entity.toDomainModel
import com.kenshi.domain.model.Product
import com.kenshi.domain.repository.LikeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val likeDao: LikeDao
) : LikeRepository {
    override fun getLikeProduct(): Flow<List<Product>> {
        return likeDao.getAll().map { entities ->
            entities.map {
                it.toDomainModel()
            }
        }
    }
}