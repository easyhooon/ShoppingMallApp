package com.kenshi.delegate

import androidx.navigation.NavHostController
import com.kenshi.domain.model.Product

interface ProductDelegate {
    fun openProduct(navHostController: NavHostController, product: Product)

    fun likeProduct(product: Product)
}