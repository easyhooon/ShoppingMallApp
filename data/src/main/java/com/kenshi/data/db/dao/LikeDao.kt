package com.kenshi.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kenshi.data.db.entity.LikeProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LikeDao {
    // like 값이 업데이트 될때마다 getAll 을 통해 entityList 를 받아옴
    @Query("SELECT * FROM like")
    fun getAll(): Flow<List<LikeProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item : LikeProductEntity)

    @Query("DELETE FROM like WHERE productId=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM like")
    suspend fun deleteAll()
}