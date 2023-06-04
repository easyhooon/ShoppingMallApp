package com.kenshi.domain.model

data class Price(
    val originalPrice: Int,
    val finalPrice: Int,
    val salesStatus: SalesStatus
)
