package com.kenshi.ui.product_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenshi.presentation.R
import com.kenshi.ui.theme.Purple200
import com.kenshi.utils.NumberUtils
import com.kenshi.viewmodel.product_detail.ProductDetailViewModel

@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val product by viewModel.product.collectAsState()

    // api 를 호출하는 방식 화면에 진입할때
    LaunchedEffect(key1 = productId) {
        viewModel.updateProduct(productId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.product_image),
                contentDescription = "product_image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            elevation = 0.dp,
            shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .size(50.dp),
                        shape = CircleShape
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.product_image),
                            contentDescription = "product_image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${product?.shop?.shopName}",
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "${product?.productName}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "${product?.productName}의 상품 상세 페이지 설명글입니다.",
                    fontSize = 12.sp
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${NumberUtils.numberFormatPrice(product?.price?.finalPrice)} 원",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = { viewModel.addBasket(product) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Purple200
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        "shopping_cart_icon"
                    )
                    Text(
                        modifier = Modifier
                            .padding(5.dp),
                        fontSize = 16.sp,
                        text = "장바구니 담기"
                    )
                }
            }
        }
    }
}