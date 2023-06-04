package com.kenshi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.kenshi.delegate.BannerDelegate
import com.kenshi.delegate.CategoryDelegate
import com.kenshi.delegate.ProductDelegate
import com.kenshi.domain.model.*
import com.kenshi.domain.usecase.AccountUseCase
import com.kenshi.domain.usecase.CategoryUseCase
import com.kenshi.domain.usecase.LikeUseCase
import com.kenshi.domain.usecase.MainUseCase
import com.kenshi.model.*
import com.kenshi.ui.component.*
import com.kenshi.utils.NavigationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
    categoryUseCase: CategoryUseCase,
    private val accountUseCase: AccountUseCase,
    likeUseCase: LikeUseCase
) : ViewModel(), ProductDelegate, BannerDelegate, CategoryDelegate {

    private val _columnCount = MutableStateFlow(DEFAULT_COLUMN_COUNT)
    val columnCount: StateFlow<Int> = _columnCount

    val modelList = mainUseCase.getModelList().map(::convertToPresentationVM)
    val categories = categoryUseCase.getCategories()
    val likeProducts = likeUseCase.getLikeProducts()

    val accountInfo = accountUseCase.getAccountInfo()

    fun openSearchForm(navHostController: NavHostController) {
        // NavigationUtils.navigate(navHostController, NavigationRouteName.SEARCH)
        NavigationUtils.navigate(navHostController, SearchNav.route)
    }

    fun openBasket(navHostController: NavHostController) {
        // NavigationUtils.navigate(navHostController, NavigationRouteName.BASKET)
        NavigationUtils.navigate(navHostController, BasketNav.route)
    }

    fun openPurchaseHistory(navHostController: NavHostController) {
        NavigationUtils.navigate(navHostController, PurchaseHistoryNav.route)
    }

    fun updateColumnCount(columnCount: Int) {
        viewModelScope.launch {
            _columnCount.emit(columnCount)
        }
    }

    override fun openProduct(navHostController: NavHostController, product: Product) {
        // NavigationUtils.navigate(navHostController, NavigationRouteName.PRODUCT_DETAIL, product)
        NavigationUtils.navigate(navHostController, ProductDetailNav.navigateWithArg(product.productId))
    }

    override fun likeProduct(product: Product) {
        viewModelScope.launch {
            mainUseCase.likeProduct(product)
        }
    }

    override fun openBanner(bannerId: String) {

    }

    override fun openCategory(navHostController: NavHostController, category: Category) {
        // NavigationUtils.navigate(navHostController, NavigationRouteName.CATEGORY, category)
        NavigationUtils.navigate(navHostController, CategoryNav.navigateWithArg(category))
    }

    private fun convertToPresentationVM(list: List<BaseModel>): List<PresentationVM<out BaseModel>> {
        return list.map { model ->
            when (model) {
                is Product -> ProductVM(model, this)
                is Ranking -> RankingVM(model, this)
                is Carousel -> CarouselVM(model, this)
                is Banner -> BannerVM(model, this)
                is BannerList -> BannerListVM(model, this)
            }
        }
    }

    fun signIn(accountInfo: AccountInfo) {
        viewModelScope.launch {
            accountUseCase.signIn(accountInfo)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            accountUseCase.signOut()
        }
    }

    companion object {
        private const val DEFAULT_COLUMN_COUNT = 2
    }
}