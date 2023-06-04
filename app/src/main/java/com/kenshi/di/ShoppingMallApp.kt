package com.kenshi.di

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShoppingMallApp: Application() {

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "a5ee176d62e201ce84e38f1f4fd960f1")
    }
}