package com.kenshi.model

import androidx.navigation.NavHostController
import com.kenshi.delegate.ProductDelegate
import com.kenshi.domain.model.Product
import com.kenshi.domain.model.Ranking

class RankingVM(
    model: Ranking,
    private val productDelegate: ProductDelegate
): PresentationVM<Ranking>(model) {

    fun openRankingProduct(navHostController: NavHostController, product: Product) {
        productDelegate.openProduct(navHostController, product)
        sendRankingLog()
    }

    fun likeProduct(product: Product) {
        productDelegate.likeProduct(product)
    }

    private fun sendRankingLog() {

    }
}