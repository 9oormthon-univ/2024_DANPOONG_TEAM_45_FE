package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.di.AppCoroutineScope
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltAndroidApp
class GlobalApplication : Application() {

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    @AppCoroutineScope
    lateinit var scope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        instance = this
        KakaoSdk.init(this, BuildConfig.API_KEY)
    }

    companion object {
        lateinit var instance: GlobalApplication
            private set
    }
}