package com.kenshi.model

import com.kenshi.delegate.BannerDelegate
import com.kenshi.domain.model.BannerList

class BannerListVM(
    model: BannerList,
    private val bannerDelegate: BannerDelegate
): PresentationVM<BannerList>(model) {
    fun openBannerList(bannerId: String) {
        bannerDelegate.openBanner(bannerId)
    }
}