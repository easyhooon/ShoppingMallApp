package com.kenshi.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.kenshi.domain.model.Category
import com.kenshi.domain.model.Price
import com.kenshi.domain.model.Shop

class LikeConverter {

    private val gson = GsonBuilder().create()

    @TypeConverter
    fun fromPrice(value: Price): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toPrice(value: String): Price {
        //TODO 왜 java class 지 -> Price::class 로 하면 에러 발생
        return gson.fromJson(value, Price::class.java)
    }

    @TypeConverter
    fun fromCategory(value: Category): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCategory(value: String): Category {
        return gson.fromJson(value, Category::class.java)
    }

    @TypeConverter
    fun fromShop(value: Shop): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toShop(value: String): Shop {
        return gson.fromJson(value, Shop::class.java)
    }
}