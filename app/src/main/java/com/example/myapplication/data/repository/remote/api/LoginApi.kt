package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    //카카오 로그인
    @POST("/v1/users/login/kakao")
    suspend fun postKakaoLogin(
        @Body logInKakaoDto: LogInKakaoDto
    ): LogInKakaoResponse

}