package com.kenshi.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kenshi.domain.model.Product
import com.kenshi.model.CarouselVM
import com.kenshi.presentation.R

@Composable
fun CarouselCard(
    navHostController: NavHostController,
    presentationVM: CarouselVM
) {
    // scroll 할 때 필요
    val scrollState = rememberLazyListState()

    Column {
        Text(
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            text = presentationVM.model.title,
            modifier = Modifier.padding(10.dp)
        )

        LazyRow(
            state = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            items(presentationVM.model.productList.size) {
                CarouselProductCard(product = presentationVM.model.productList[it], presentationVM) { product ->
                    presentationVM.openCarouselProduct(navHostController, product)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CarouselProductCard(
    product: Product,
    presentationVM: CarouselVM,
    onClick: (Product) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(150.dp)
            .wrapContentHeight()
            .padding(10.dp),
        onClick = { onClick(product) }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { presentationVM.likeProduct(product) },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    if (product.isLike) Icons.Filled.Favorite
                    else Icons.Filled.FavoriteBorder,
                    contentDescription = "favorite_icon"
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                    text = product.productName
                )
                Price(product)
            }
        }
    }
}
