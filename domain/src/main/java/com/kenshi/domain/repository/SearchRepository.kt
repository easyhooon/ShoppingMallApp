package com.kenshi.domain.repository

import com.kenshi.domain.model.Product
import com.kenshi.domain.model.SearchFilter
import com.kenshi.domain.model.SearchKeyword
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun search(
        searchKeyword: SearchKeyword,
        filters: List<SearchFilter>
    ): Flow<List<Product>>

    fun getSearchKeywords(): Flow<List<SearchKeyword>>

    suspend fun likeProduct(product: Product)
}