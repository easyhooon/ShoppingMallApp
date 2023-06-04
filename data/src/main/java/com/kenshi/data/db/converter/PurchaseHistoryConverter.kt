package com.kenshi.data.db.converter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kenshi.domain.model.BasketProduct
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class PurchaseHistoryConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromBasketProductList(value: List<BasketProduct>): String {
        return gson.toJson(value, object : TypeToken<List<BasketProduct>>() {}.type)
    }

    @TypeConverter
    fun toBasketProductList(value: String): List<BasketProduct> {
        return gson.fromJson(value, object : TypeToken<List<BasketProduct>>() {}.type)
    }

    //TODO zonedDateTime 을 String type 으로 변경하는 방법
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromZonedDateTime(value: ZonedDateTime): String {
        return DateTimeFormatter.ISO_ZONED_DATE_TIME.format(value)
    }

    //TODO String Type 을 zonedDateTime 으로 변경하는 방법
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toZonedDateTime(value: String): ZonedDateTime {
        return ZonedDateTime.parse(value)
    }
}