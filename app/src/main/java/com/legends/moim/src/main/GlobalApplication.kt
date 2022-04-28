package com.legends.moim.src.main

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "d04b5837217105d4b2a2b16ced721572")

    }
}