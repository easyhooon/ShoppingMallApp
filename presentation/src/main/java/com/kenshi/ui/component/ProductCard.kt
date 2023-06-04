package com.kenshi.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kenshi.delegate.ProductDelegate
import com.kenshi.domain.model.*
import com.kenshi.model.ProductVM
import com.kenshi.presentation.R
import com.kenshi.ui.theme.Purple200

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductCard(
    navHostController: NavHostController,
    presentationVM: ProductVM
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(10.dp)
            .shadow(elevation = 10.dp),
        onClick = { presentationVM.openProduct(navHostController, presentationVM.model) }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { presentationVM.likeProduct(presentationVM.model) },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    if (presentationVM.model.isLike) Icons.Filled.Favorite
                    else Icons.Filled.FavoriteBorder,
                    contentDescription = "favorite_icon"
                )
            }
            Column(
                // TODO height의 크기를 지정하지 않아야 테스트가 짤리지 않는 이유
                modifier = Modifier
                    .fillMaxWidth()
                    // .height(280.dp)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Image(
                    painterResource(id = R.drawable.product_image),
                    "product_image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                Text(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = presentationVM.model.shop.shopName
                )
                Text(
                    fontSize = 14.sp,
                    text = presentationVM.model.productName
                )
                // 할인 분기 처리
                Price(presentationVM.model)
            }
        }
    }
}

@Composable
fun Price(product: Product) {
    when (product.price.salesStatus) {
        SalesStatus.ON_SALE -> {
            Text(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = "${product.price.originalPrice}원"
            )
        }
        SalesStatus.ON_DISCOUNT -> {
            Text(
                fontSize = 14.sp,
                text = "${product.price.originalPrice}원",
                style = TextStyle(textDecoration = TextDecoration.LineThrough)
            )
            Text(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Purple200,
                text = "${product.price.finalPrice}원"
            )
        }
        SalesStatus.SOLD_OUT -> {
            Text(
                fontSize = 18.sp,
                color = Color(0xFF666666),
                text = "판매종료"
            )
        }
    }
}

@Preview
@Composable
fun ProductCardPreview() {
    ProductCard(rememberNavController(),
        ProductVM(
            model = Product(
                productId = "1",
                productName = "상품 이름",
                imageUrl = "",
                price = Price(
                    originalPrice = 30000,
                    finalPrice = 30000,
                    salesStatus = SalesStatus.ON_SALE
                ),
                category = Category.Top,
                shop = Shop(
                    shopId = "1",
                    shopName = "샵 이름",
                    imageUrl = "",
                ),
                isNew = false,
                isFreeShipping = false,
                isLike = false
            ),
            object : ProductDelegate {
                override fun openProduct(navHostController: NavHostController, product: Product) {

                }

                override fun likeProduct(product: Product) {

                }
            }
        )
    )
}

@Preview
@Composable
private fun ProductDiscountCardPreview() {
    ProductCard(rememberNavController(),
        ProductVM(
            model = Product(
                productId = "1",
                productName = "상품 이름",
                imageUrl = "",
                price = Price(
                    originalPrice = 30000,
                    finalPrice = 20000,
                    salesStatus = SalesStatus.ON_DISCOUNT
                ),
                category = Category.Top,
                shop = Shop(
                    shopId = "1",
                    shopName = "샵 이름",
                    imageUrl = "",
                ),
                isNew = false,
                isFreeShipping = false,
                isLike = false
            ),
            object : ProductDelegate {
                override fun openProduct(navHostController: NavHostController, product: Product) {

                }

                override fun likeProduct(product: Product) {

                }
            }
        )
    )
}

@Preview
@Composable
private fun ProductSoldOutCardPreview() {
    ProductCard(rememberNavController(),
        ProductVM(
            model = Product(
                productId = "1",
                productName = "상품 이름",
                imageUrl = "",
                price = Price(
                    originalPrice = 30000,
                    finalPrice = 20000,
                    salesStatus = SalesStatus.SOLD_OUT
                ),
                category = Category.Top,
                shop = Shop(
                    shopId = "1",
                    shopName = "샵 이름",
                    imageUrl = "",
                ),
                isNew = false,
                isFreeShipping = false,
                isLike = false
            ),
            object : ProductDelegate {
                override fun openProduct(navHostController: NavHostController, product: Product) {

                }

                override fun likeProduct(product: Product) {

                }
            }
        )
    )
}

