package com.kenshi.ui.category

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kenshi.domain.model.Category
import com.kenshi.ui.component.ProductCard
import com.kenshi.viewmodel.category.CategoryViewModel

//TODO 카테고리 화면에서 상품 카드를 눌렀을때 해당 상품 리스트가 출력되지 않는 문제
@Composable
fun CategoryScreen(
    category: Category,
    navHostController: NavHostController,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState()
    // 컴포지션될 때 호출
    // 키 값이 변경되면 기존의 코루틴을 취소하고 다시 호출
    LaunchedEffect(key1 = category) {
        viewModel.updateCategory(category)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(products.size) { index ->
            ProductCard(navHostController = navHostController, presentationVM = products[index])
        }
    }
}