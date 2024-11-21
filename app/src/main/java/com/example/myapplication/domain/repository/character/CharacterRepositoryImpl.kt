package com.example.myapplication.domain.repository.character

import com.example.myapplication.data.repository.remote.datasource.remote.CharacterDataSource
import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterAllResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterResponse
import com.example.myapplication.data.repository.remote.response.character.CommitCharacterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterDataSource: CharacterDataSource
) : CharacterRepository {
    override suspend fun postCharacter(characterDTO: CharacterDTO): Flow<BaseResponse<CommitCharacterResponse>> =
        characterDataSource.postCharacter(characterDTO)

    override suspend fun getCharacter(id: String): Flow<CharacterResponse> =
        characterDataSource.getCharacter(id)

    override suspend fun getAllCharacter(): Flow<CharacterAllResponse> =
        characterDataSource.getAllCharacter()

    override suspend fun deleteCharacter(id: String): Flow<Response<ResponseBody>> =
        characterDataSource.deleteCharacter(id)

    override suspend fun putCharacter(character_id: Int, name: String): Flow<BaseResponse<Any>> =
        characterDataSource.putCharacter(character_id, name)

    override suspend fun postIncreaseActivityPoint(
        character_id: Int,
        activityPoint: Int
    ): Flow<BaseResponse<Any>> =
        characterDataSource.postIncreaseActivityPoint(character_id, activityPoint)

    override suspend fun postDecreaseActivityPoint(
        character_id: Int,
        activityPoint: Int
    ): Flow<BaseResponse<Any>> =
        characterDataSource.postDecreaseActivityPoint(character_id, activityPoint)
}