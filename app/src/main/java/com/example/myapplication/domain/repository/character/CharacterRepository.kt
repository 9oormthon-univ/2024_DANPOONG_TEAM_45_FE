package com.example.myapplication.domain.repository.character

import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterAllResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterRandomResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterResponse
import com.example.myapplication.data.repository.remote.response.character.CommitCharacterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface CharacterRepository {
    //캐릭터 등록
    suspend fun postCharacter(characterDTO: CharacterDTO
    ) : Flow<BaseResponse<CommitCharacterResponse>>

    //캐릭터 단 건 조회
    suspend fun getCharacter(
        id : String
    ) : Flow<CharacterResponse>

    //캐릭터 모두 조회
    suspend fun getAllCharacter(
    ) : Flow<CharacterAllResponse>

    //캐릭터 삭제
    suspend fun deleteCharacter(
        id : String
    ) : Flow<Response<ResponseBody>>

    //캐릭터 이름 수정
    suspend fun putCharacter(
        character_id : Int,
        name : String
    ) : Flow<BaseResponse<Any>>

    //활동 포인트 증가
    suspend fun postIncreaseActivityPoint(
        character_id : Int,
        activityPoint : Int
    ) : Flow<BaseResponse<Any>>

    //활동 포인트 감소
    suspend fun postDecreaseActivityPoint(
        character_id : Int,
        activityPoint : Int
    ) : Flow<BaseResponse<Any>>

    // 랜덤 선인장 얻기
    suspend fun getRandomCactus(
    ) : Flow<BaseResponse<CharacterRandomResponse>>
}