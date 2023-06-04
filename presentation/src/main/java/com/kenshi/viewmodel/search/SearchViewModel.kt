package com.kenshi.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kenshi.delegate.ProductDelegate
import com.kenshi.domain.model.Product
import com.kenshi.domain.model.SearchFilter
import com.kenshi.domain.model.SearchKeyword
import com.kenshi.domain.usecase.SearchUseCase
import com.kenshi.model.ProductVM
import com.kenshi.ui.component.SearchNav
import com.kenshi.utils.NavigationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel(), ProductDelegate {

    private val searchManager = SearchManager()

    private val _searchResult = MutableStateFlow<List<ProductVM>>(listOf())
    val searchResult: StateFlow<List<ProductVM>> = _searchResult

    val searchKeywords = searchUseCase.getSearchKeywords()

    val searchFilters = searchManager.filters

    fun search(searchKeyword: String) {
        viewModelScope.launch {
            searchInternalSearchKeyword(searchKeyword)
        }
    }

    override fun likeProduct(product: Product) {
        viewModelScope.launch {
            searchUseCase.likeProduct(product)
        }
    }

    fun updateFilter(filter: SearchFilter) {
        viewModelScope.launch {
            searchManager.updateFilter(filter)
            searchInternal()
        }
    }

    // keyword 없이 업데이트
    private suspend fun searchInternal() {
        searchUseCase.search(
            searchKeyword = searchManager.searchKeyword,
            filters = searchManager.currentFilters()
        ).collectLatest {
            _searchResult.emit(it.map(::convertToProductVM))
        }
    }

    private suspend fun searchInternalSearchKeyword(newSearchKeyword: String = "") {
        // 이전 필터 값 제거(필터 초기화)
        searchManager.clearFilter()

        searchUseCase.search(
            searchKeyword = SearchKeyword(newSearchKeyword),
            filters = searchManager.currentFilters()
        ).collectLatest {
            if (newSearchKeyword.isNotEmpty()) {
                searchManager.initSearchManager(newSearchKeyword, it)
            }

            _searchResult.emit(it.map(::convertToProductVM))
        }
    }

    private fun convertToProductVM(product: Product): ProductVM {
        return ProductVM(product, this)
    }


    // TODO 이거 안드로이드 종속성 있는거 아님?
    override fun openProduct(navHostController: NavHostController, product: Product) {
        NavigationUtils.navigate(navHostController, SearchNav.route)
    }
}