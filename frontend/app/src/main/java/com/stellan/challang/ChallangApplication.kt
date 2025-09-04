package com.stellan.challang

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.stellan.challang.data.model.auth.TokenProvider

class ChallangApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        TokenProvider.init(this)

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}

