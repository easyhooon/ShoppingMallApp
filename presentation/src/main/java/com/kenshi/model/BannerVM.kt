package com.kenshi.model

import com.kenshi.delegate.BannerDelegate
import com.kenshi.domain.model.Banner

class BannerVM(model: Banner, bannerDelegate: BannerDelegate):
    PresentationVM<Banner>(model), BannerDelegate by bannerDelegate