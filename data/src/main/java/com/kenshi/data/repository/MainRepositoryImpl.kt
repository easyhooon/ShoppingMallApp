package com.kenshi.data.repository

import com.kenshi.data.datasource.ProductDataSource
import com.kenshi.data.db.dao.LikeDao
import com.kenshi.data.db.entity.toLikeProductEntity
import com.kenshi.domain.model.BaseModel
import com.kenshi.domain.model.Carousel
import com.kenshi.domain.model.Product
import com.kenshi.domain.model.Ranking
import com.kenshi.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dataSource: ProductDataSource,
    private val likeDao: LikeDao
) : MainRepository {
    // json asset 을 mock data 로 하여 데이터를 api 를 대체하는 임시 데이터 가져오기
    // TODO 내가 궁금했었던 좋아요 구현법
    // datasource 에서 값을 가져오는 것과 dao 에서 like 받은 값을 combine 하여 가져옴
    // combine -> 하나만 업데이트 되도 업데이트 됨
    override fun getModelList(): Flow<List<BaseModel>> {
        return dataSource.getHomeComponents().combine(likeDao.getAll()) { homeComponents, likeList ->
            homeComponents.map { model -> mappingLike(model, likeList.map { it.productId }) }
        }
    }

    override suspend fun likeProduct(product: Product) {
        if (product.isLike) {
            likeDao.delete(product.productId)
        } else {
            likeDao.insert(product.toLikeProductEntity().copy(isLike = true))
        }
    }

    private fun mappingLike(baseModel: BaseModel, likeProductIds: List<String>): BaseModel {
        return when (baseModel) {
            is Carousel -> {
                baseModel.copy(productList = baseModel.productList.map { updateLikeStatus(it, likeProductIds) })
            }
            is Ranking -> {
                baseModel.copy(productList = baseModel.productList.map { updateLikeStatus(it, likeProductIds) })
            }
            is Product -> {
                updateLikeStatus(baseModel, likeProductIds)
            }
            else -> baseModel
        }
    }

    private fun updateLikeStatus(product: Product, likeProductIds: List<String>): Product {
        return product.copy(isLike = likeProductIds.contains(product.productId))
    }
}