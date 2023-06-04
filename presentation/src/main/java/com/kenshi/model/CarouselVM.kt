package com.kenshi.model

import androidx.navigation.NavHostController
import com.kenshi.delegate.ProductDelegate
import com.kenshi.domain.model.Carousel
import com.kenshi.domain.model.Product

class CarouselVM(model: Carousel, private val productDelegate: ProductDelegate): PresentationVM<Carousel>(model) {

    //TODO 꼭 navHostConroller 를 전달해야하나..
    fun openCarouselProduct(navHostController: NavHostController, product: Product) {
        productDelegate.openProduct(navHostController, product)
        sendCarouselLog()
    }

    fun likeProduct(product: Product) {
        productDelegate.likeProduct(product)
    }

    private fun sendCarouselLog() {

    }
}