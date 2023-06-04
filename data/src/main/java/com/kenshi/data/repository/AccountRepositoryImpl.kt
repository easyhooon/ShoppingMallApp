package com.kenshi.data.repository

import com.kenshi.data.datasource.PreferenceDatasource
import com.kenshi.data.db.dao.BasketDao
import com.kenshi.data.db.dao.LikeDao
import com.kenshi.domain.model.AccountInfo
import com.kenshi.domain.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val preferenceDatasource: PreferenceDatasource,
    private val basketDao: BasketDao,
    private val likeDao: LikeDao
) : AccountRepository {
    private val accountInfoFlow = MutableStateFlow(preferenceDatasource.getAccountInfo())

    override fun getAccountInfo(): StateFlow<AccountInfo?> {
        return accountInfoFlow
    }

    override suspend fun signIn(accountInfo: AccountInfo) {
        preferenceDatasource.putAccountInfo(accountInfo)
        accountInfoFlow.emit(accountInfo)
    }

    override suspend fun signOut() {
        preferenceDatasource.removeAccountInfo()
        accountInfoFlow.emit(null)
        // 로그아웃 하면 장바구니에 담았던 모든 항목들이 지워지도록
        basketDao.deleteAll()
        // 로그아웃 하면 좋아요를 표시한 모든 항목들이 초기화되도록
        likeDao.deleteAll()
    }
}