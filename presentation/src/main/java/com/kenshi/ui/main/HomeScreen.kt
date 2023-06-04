package com.kenshi.ui.main

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.kenshi.domain.model.*
import com.kenshi.model.*
import com.kenshi.ui.component.*
import com.kenshi.viewmodel.MainViewModel

@Composable
fun MainHomeScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    // stateFlow 를 state 로 변환하면 초깃값을 필요로 하지 않음
    // flow 이기 때문에 초깃값이 필요
    val modelList by viewModel.modelList.collectAsState(initial = listOf())
    val columnCount by viewModel.columnCount.collectAsState()

    //TODO LazyVerticalGrid 사용 방법
    LazyVerticalGrid(columns = GridCells.Fixed(columnCount)) {
        items(modelList.size, span = { index ->
            val item = modelList[index]
            val spanCount = getSpanByType(item.model.type, columnCount)

            GridItemSpan(spanCount)
        }) {
            when (val item = modelList[it]) {
                is BannerVM -> BannerCard(presentationVM = item)
                is BannerListVM -> BannerListCard(presentationVM = item)
                is ProductVM -> ProductCard(navHostController = navController, presentationVM = item)
                is CarouselVM -> CarouselCard(navHostController = navController, presentationVM = item)
                is RankingVM -> RankingCard(navHostController = navController, presentationVM = item)
            }
        }
    }
}

private fun getSpanByType(type: ModelType, defaultColumnCount: Int): Int {
    return when (type) {
        ModelType.PRODUCT -> 1
        ModelType.BANNER, ModelType.BANNER_LIST,
        ModelType.CAROUSEL, ModelType.RANKING-> defaultColumnCount
    }
}