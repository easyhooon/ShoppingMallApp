package com.kenshi.data.repository

import com.kenshi.data.datasource.ProductDataSource
import com.kenshi.data.db.dao.LikeDao
import com.kenshi.data.db.dao.SearchDao
import com.kenshi.data.db.entity.SearchKeywordEntity
import com.kenshi.data.db.entity.toDomain
import com.kenshi.data.db.entity.toLikeProductEntity
import com.kenshi.domain.model.Product
import com.kenshi.domain.model.SearchKeyword
import com.kenshi.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val dataSource: ProductDataSource,
    private val searchDao: SearchDao,
    private val likeDao: LikeDao
) : SearchRepository {

    override suspend fun search(searchKeyword: SearchKeyword): Flow<List<Product>> {
        // 검색어를 저장
        searchDao.insert(SearchKeywordEntity(searchKeyword.keyword))
        return dataSource.getProducts().combine(likeDao.getAll()) { products, likeList ->
                products.map { product -> updateLikeStatus(product, likeList.map { it.productId }) }
            }
    }

    override fun getSearchKeywords(): Flow<List<SearchKeyword>> {
        return searchDao.getAll().map {
            it.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun likeProduct(product: Product) {
        if (product.isLike) {
            likeDao.delete(product.productId)
        } else {
            likeDao.insert(product.toLikeProductEntity().copy(isLike = true))
        }
    }

    private fun updateLikeStatus(product: Product, likeProductIds: List<String>): Product {
        return product.copy(isLike = likeProductIds.contains(product.productId))
    }
}