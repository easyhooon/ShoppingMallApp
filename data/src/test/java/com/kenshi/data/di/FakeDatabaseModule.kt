package com.kenshi.data.di

import android.content.Context
import androidx.room.Room
import com.kenshi.data.db.ApplicationDatabase
import com.kenshi.data.db.dao.BasketDao
import com.kenshi.data.db.dao.LikeDao
import com.kenshi.data.db.dao.PurchaseHistoryDao
import com.kenshi.data.db.dao.SearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// DataBaseModule 을 참조할 수 있는 경우, 현재는 불가능
//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [DatabaseModule::class]
//)

@Module
@InstallIn(SingletonComponent::class)
class FakeDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ApplicationDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ApplicationDatabase::class.java,
            ApplicationDatabase.DB_NAME
        )
            // 기존의 데이터를 보존하지 않고 업데이트
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchDao(database: ApplicationDatabase): SearchDao {
        return database.searchDao()
    }

    @Provides
    @Singleton
    fun provideLikeDao(database: ApplicationDatabase): LikeDao {
        return database.likeDao()
    }

    @Provides
    @Singleton
    fun provideBasketDao(database: ApplicationDatabase): BasketDao {
        return database.basketDao()
    }

    @Provides
    @Singleton
    fun providePurchaseHistoryDao(database: ApplicationDatabase): PurchaseHistoryDao {
        return database.purchaseHistoryDao()
    }
}