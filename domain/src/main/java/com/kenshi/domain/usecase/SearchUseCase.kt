package com.kenshi.domain.usecase

import com.kenshi.domain.model.Product
import com.kenshi.domain.model.SearchFilter
import com.kenshi.domain.model.SearchKeyword
import com.kenshi.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend fun search(searchKeyword: SearchKeyword, filters: List<SearchFilter>): Flow<List<Product>> {
        return searchRepository.search(searchKeyword).map { list ->
            list.filter { isAvailableProduct(it, searchKeyword, filters) }
        }
    }

    fun getSearchKeywords(): Flow<List<SearchKeyword>> {
        return searchRepository.getSearchKeywords()
    }

    suspend fun likeProduct(product: Product) {
        searchRepository.likeProduct(product)
    }

    private fun isAvailableProduct(
        product: Product,
        searchKeyword: SearchKeyword,
        filters: List<SearchFilter>
    ): Boolean {
        var isAvailable = true
        filters.forEach {
            isAvailable = isAvailable && it.isAvailableProduct(product)
        }

        return isAvailable && product.productName.contains(searchKeyword.keyword)
    }
}