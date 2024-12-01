package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterAllResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterRandomResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterResponse
import com.example.myapplication.data.repository.remote.response.character.CommitCharacterResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterApi {

    //캐릭터 이름 수정
    @PUT("/v1/api/character/{character_id}")
    suspend fun putCharacter(
        @Path("character_id") characterId : Int,
        @Query("name") name: String
    ): BaseResponse<Any>

    //캐릭터 등록
    @POST("/v1/api/character")
    suspend fun postCharacter(
        @Body characterDTO: CharacterDTO
    ): BaseResponse<CommitCharacterResponse>

    //활동 포인트 증가
    @POST("/v1/api/character/increaseActivityPoint")
    suspend fun postIncreaseActivityPoint(
        @Query("character_id") character_id : Int,
        @Query("activityPoint") activityPoint : Int
    ): BaseResponse<Any>

    //활동 포인트 감소
    @POST("/v1/api/character/decreaseActivityPoint")
    suspend fun postDecreaseActivityPoint(
        @Query("character_id") character_id : Int,
        @Query("activityPoint") activityPoint : Int
    ): BaseResponse<Any>

    //캐릭터 단 건 조회
    @GET("/character")
    suspend fun getCharacter(
        @Path("id") id: String
    ): CharacterResponse

    //캐릭터 모두 조회
    @GET("/character/all")
    suspend fun getAllCharacter(
    ): CharacterAllResponse

    //캐릭터 랜덤 뽑기
    @GET("/v1/api/character/pickup/random")
    suspend fun getRandomCactus(
    ): BaseResponse<CharacterRandomResponse>

    //캐릭터 삭제
    @DELETE("/character")
    suspend fun deleteCharacter(
        @Path("id") id: String
    ): Response<ResponseBody>
}