package com.kenshi.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kenshi.data.db.entity.SearchKeywordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    // 검색을 할때 마다 키워드를 갱신
    @Query("SELECT * FROM search")
    fun getAll(): Flow<List<SearchKeywordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SearchKeywordEntity)
}