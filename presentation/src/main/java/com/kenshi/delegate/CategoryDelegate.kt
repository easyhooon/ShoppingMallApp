package com.kenshi.delegate

import androidx.navigation.NavHostController
import com.kenshi.domain.model.Category

interface CategoryDelegate {
    fun openCategory(navHostController: NavHostController, category: Category)
}