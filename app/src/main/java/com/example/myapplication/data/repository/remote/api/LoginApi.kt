package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.request.login.UserDTO
import com.example.myapplication.data.repository.remote.request.login.UserListDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface LoginApi {

    //카카오 로그인
    @POST("/v1/users/login/kakao")
    suspend fun postKakaoLogin(
        @Body logInKakaoDto: LogInKakaoDto
    ): LogInKakaoResponse

    @PATCH("/v1/users")
    suspend fun patchUsers(
        @Body userDTO: UserDTO
    ) : BaseResponse<Any>

    @POST("/v1/users/{user_id}")
    suspend fun getUser(
        @Path("user_id") user_id : Int
    ) : BaseResponse<UserDTO>

    @GET("/v1/users/complete/training")
    suspend fun getCompleteTraining(
    ) : BaseResponse<Any>

    @GET("/v1/users/check/training")
    suspend fun checkTraining(
    ) : BaseResponse<Boolean>

    @GET("v1/users/all")
    suspend fun getAllUser(
    ) : BaseResponse<UserListDTO>

}