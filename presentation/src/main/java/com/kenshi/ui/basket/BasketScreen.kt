package com.kenshi.ui.basket

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenshi.domain.model.BasketProduct
import com.kenshi.domain.model.Product
import com.kenshi.presentation.R
import com.kenshi.ui.component.Price
import com.kenshi.ui.popupSnackBar
import com.kenshi.ui.theme.Purple200
import com.kenshi.utils.NumberUtils
import com.kenshi.viewmodel.basket.BasketAction
import com.kenshi.viewmodel.basket.BasketEvent
import com.kenshi.viewmodel.basket.BasketViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable
fun BasketScreen(
    scaffoldState: ScaffoldState,
    viewModel: BasketViewModel = hiltViewModel(),
) {
    val basketProducts by viewModel.basketProducts.collectAsState(initial = listOf())
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is BasketEvent.ShowSnackBar -> {
                    popupSnackBar(scope, scaffoldState, "결제 되었습니다.")
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(basketProducts.size) { index ->
                BasketProductCard(
                    basketProduct = basketProducts[index]
                ) {
                    //viewModel.deleteBasketProduct(it)
                    viewModel.dispatch(BasketAction.DeleteProduct(it))
                }
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.dispatch(BasketAction.CheckoutBasket(basketProducts)) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Purple200
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Check, "check_icon")

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                fontSize = 16.sp,
                text = "${getTotalPrice(basketProducts)} 결제하기"
            )
        }
    }
}

@Composable
fun BasketProductCard(
    basketProduct: BasketProduct,
    deleteProduct: (Product) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.product_image),
                "product_image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .aspectRatio(1f)
            )
            Column(
                modifier = Modifier
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
                    //TODO weight 있고 없고 차이
                    .weight(1f)
            ) {
                Text(
                    fontSize = 14.sp,
                    text = basketProduct.product.shop.shopName,
                    modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                )
                Text(
                    fontSize = 14.sp,
                    text = basketProduct.product.productName,
                    modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                )
                Price(product = basketProduct.product)
            }
            Text(
                text = "${basketProduct.count} 개",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(30.dp)
            )
        }
        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = { deleteProduct(basketProduct.product) }
        ) {
            Icon(Icons.Filled.Close, "close_icon")
        }
    }
}

private fun getTotalPrice(list: List<BasketProduct>): String {
    val totalPrice = list.sumOf { it.product.price.finalPrice * it.count }
    return NumberUtils.numberFormatPrice(totalPrice)
}