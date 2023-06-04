package com.kenshi.viewmodel.purchase_history

import androidx.lifecycle.ViewModel
import com.kenshi.domain.usecase.PurchaseHistoryUseCase
import javax.inject.Inject

class PurchaseHistoryViewModel @Inject constructor(
    useCase: PurchaseHistoryUseCase
) : ViewModel() {

    val purchaseHistory = useCase.getPurchaseHistory()
}