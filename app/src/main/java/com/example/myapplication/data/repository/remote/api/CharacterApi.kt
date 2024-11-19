package com.example.myapplication.data.repository.remote.api

import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.request.home.PatchHomeDTO
import com.example.myapplication.data.repository.remote.response.character.CharacterAllResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterResponse
import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CharacterApi {

    //캐릭터 등록
    @POST("/character")
    suspend fun postCharacter(
        @Body characterDTO: CharacterDTO
    ) : Response<ResponseBody>

    //캐릭터 단 건 조회
    @GET("/character")
    suspend fun getCharacter(
        @Path("id") id : String
    ) : CharacterResponse

    //캐릭터 모두 조회
    @GET("/character/all")
    suspend fun getAllCharacter(
    ) : CharacterAllResponse

    //캐릭터 수정
    @PATCH("/character")
    suspend fun patchCharacter(
        @Body characterDTO: CharacterDTO
    ) : Response<ResponseBody>

    //캐릭터 삭제
    @DELETE("/character")
    suspend fun deleteCharacter(
        @Path("id") id : String
    )
}