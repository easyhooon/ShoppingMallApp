package com.kenshi.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kenshi.data.db.entity.BasketProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BasketDao {

    // 수정을 반영하는데 suspend 보다 flow 가 유리
    @Query("SELECT * FROM basket")
    fun getAll(): Flow<List<BasketProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BasketProductEntity)

    @Query("SELECT * FROM basket WHERE productId=:id")
    suspend fun get(id: String): BasketProductEntity?

    @Query("DELETE FROM basket WHERE ProductId=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM basket")
    suspend fun deleteAll()
}
