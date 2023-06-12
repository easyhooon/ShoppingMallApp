package com.kenshi.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kenshi.data.db.dao.*
import com.kenshi.data.db.entity.*

@Database(
    entities = [
        PurchaseProductEntity::class,
        LikeProductEntity::class,
        BasketProductEntity::class,
        SearchKeywordEntity::class,
        PurchaseHistoryEntity::class,
    ],
    version = 3,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {
    companion object {
        const val DB_NAME = "ApplicationDatabase.db"
    }

    abstract fun PurchaseDao() : PurchaseDao

    abstract fun likeDao() : LikeDao

    abstract fun basketDao() : BasketDao

    abstract fun searchDao() : SearchDao

    abstract fun purchaseHistoryDao() : PurchaseHistoryDao
}