package com.kenshi.ui.purchase_history

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenshi.domain.model.BasketProduct
import com.kenshi.domain.model.PurchaseHistory
import com.kenshi.presentation.R
import com.kenshi.ui.component.Price
import com.kenshi.utils.NumberUtils
import com.kenshi.viewmodel.purchase_history.PurchaseHistoryViewModel


// TODO LazyColumnn 은 중첩으로 동작하지 않는다!!
// -> 이럴 경우 해결법
// 내부 LazyColumn 제거
// Card 를 LazyListScope 의 함수 형태로 변환 (@Composable annotation 제거)
// imtes 가 아닌 forEach 구문으로 Card 를 받아준다
// TODO 이러면 무분별한 recomposition 이 발생하는 것이 아닌가? -> Card 내부에서 핸들링 해주는듯
@Composable
fun PurchaseHistoryScreen(
    viewModel: PurchaseHistoryViewModel = hiltViewModel()
) {
    val purchaseHistory by viewModel.purchaseHistory.collectAsState(initial = listOf())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        purchaseHistory.forEach {
            PurchaseHistoryCard(it)
        }
//        items(purchaseHistory.size) { index ->
//            PurchaseHistoryCard(purchaseHistory = purchaseHistory[index])
//        }
    }
}


fun LazyListScope.PurchaseHistoryCard(
    purchaseHistory: PurchaseHistory
) {

    item {
        Text(
            fontSize = 16.sp,
            text = "결제 시기: ${purchaseHistory.purchaseAt}"
        )
    }
    items(purchaseHistory.basketList.size) { index ->
        val currentItem = purchaseHistory.basketList[index]

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.product_image),
                contentDescription = "purchase_item",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp)
            )
            Column(
                modifier = Modifier
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
                    .weight(1f)
            ) {
                Text(
                    fontSize = 14.sp,
                    text = "${currentItem.product.shop.shopName} - ${currentItem.product.productName}",
                )
            }
            Price(product = currentItem.product)
        }
        Text(
            text = "${currentItem.count} 개",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(30.dp, 0.dp, 0.dp, 0.dp)
        )
    }
    item {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 20.dp),
            fontSize = 16.sp,
            text = "${getTotalPrice(purchaseHistory.basketList)} 결제완료"
        )
    }
}

private fun getTotalPrice(list: List<BasketProduct>): String {
    val totalPrice = list.sumOf { it.product.price.finalPrice * it.count }
    return NumberUtils.numberFormatPrice(totalPrice)
}