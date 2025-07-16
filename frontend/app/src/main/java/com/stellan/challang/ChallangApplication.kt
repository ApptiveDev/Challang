package com.stellan.challang

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.stellan.challang.data.model.auth.TokenProvider  // ✅ TokenProvider import

class ChallangApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // okenProvider 초기화 추가
        TokenProvider.init(this)

        // 카카오 SDK 초기화
        KakaoSdk.init(this, "")
    }
}

