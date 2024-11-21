package com.example.myapplication.presentation.widget.extention

import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getAccessToken.firstOrNull() // Flow에서 첫 번째 값을 가져옴
        }
        val requestBuilder = chain.request().newBuilder()

        val jwtTokenRegex = """^[A-Za-z0-9-_]+\.[A-Za-z0-9-_]+\.[A-Za-z0-9-_]+$""".toRegex()
        if (token != null && token.matches(jwtTokenRegex)) {
            // JWT 토큰 형식이 유효한 경우
            val formattedToken = "$TOKEN_TYPE $token"
            requestBuilder.addHeader(HEADER_AUTHORIZATION, formattedToken)
            Log.d("토큰", "유효한 JWT 토큰: $formattedToken")
        } else {
            // 토큰이 없거나 형식이 유효하지 않은 경우
            Log.e("토큰", "잘못된 토큰 형식 또는 토큰 없음")
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
