package com.kenshi.viewmodel.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenshi.domain.model.BasketProduct
import com.kenshi.domain.model.Product
import com.kenshi.domain.usecase.BasketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val basketUseCase: BasketUseCase
) : ViewModel() {
    val basketProducts = basketUseCase.getBasketProducts()

    // 상권님 방법 응용인듯
    // 하나의 eventFlow 로 모든 이벤트를 처리
    private val _eventFlow = MutableSharedFlow<BasketEvent>()
    val eventFlow: SharedFlow<BasketEvent> = _eventFlow

    // 전체 이벤트를 이 dispatch 함수만을 봐도 확인할 수 있음
    fun dispatch(action: BasketAction) {
        when (action) {
            is BasketAction.DeleteProduct -> {
                deleteBasketProduct(action.product)
            }
            is BasketAction.CheckoutBasket -> {
                checkoutBasket(action.products)
            }
        }
    }

    // 다 private 으로 변경
    private fun deleteBasketProduct(product: Product) {
        viewModelScope.launch {
            basketUseCase.deleteBasketProduct(product)
        }
    }

    private fun checkoutBasket(products: List<BasketProduct>) {
        viewModelScope.launch {
            basketUseCase.checkoutBasket(products)
        }
    }
}

// TODO 여러 이벤트를 관리하는 클래스(하나씩 뷰모델내에 선언하는 방법이 아닌)
// 전체적인 스펙 확인에도 용이

// viewModel -> screen
sealed class BasketEvent {
    object ShowSnackBar : BasketEvent()
}

// screen -> viewModel
sealed class BasketAction {
    data class DeleteProduct(val product: Product) : BasketAction()
    data class CheckoutBasket(val products: List<BasketProduct>) : BasketAction()
}