package com.example.myapplication.data.repository.remote.datasource.remote

import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterAllResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CharacterDataSource {
    //캐릭터 등록
    suspend fun postCharacter(characterDTO: CharacterDTO
    ) : Flow<Response<ResponseBody>>

    //캐릭터 단 건 조회
    suspend fun getCharacter(
        id : String
    ) : Flow<CharacterResponse>

    //캐릭터 모두 조회
    suspend fun getAllCharacter(
    ) : Flow<CharacterAllResponse>

    //캐릭터 수정
    suspend fun patchCharacter(
        characterDTO: CharacterDTO
    ) : Flow<Response<ResponseBody>>

    //캐릭터 삭제
    suspend fun deleteCharacter(
        id : String
    ) : Flow<Response<ResponseBody>>
}