package com.kenshi.viewmodel.product_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenshi.domain.model.Product
import com.kenshi.domain.usecase.ProductDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productDetailUseCase: ProductDetailUseCase
) : ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    suspend fun updateProduct(productId: String) {
        productDetailUseCase.getProductDetail(productId).collectLatest {
            _product.emit(it)
        }
    }

    fun addBasket(product: Product?) {
        // early return
        product ?: return

        viewModelScope.launch {
            productDetailUseCase.addBasket(product)
        }
    }
}