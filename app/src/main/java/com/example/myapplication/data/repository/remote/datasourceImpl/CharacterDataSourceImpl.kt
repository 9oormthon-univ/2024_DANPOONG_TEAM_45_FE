package com.example.myapplication.data.repository.remote.datasourceImpl

import android.util.Log
import com.example.myapplication.data.repository.remote.api.CharacterApi
import com.example.myapplication.data.repository.remote.datasource.remote.CharacterDataSource
import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterAllResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterResponse
import com.example.myapplication.data.repository.remote.response.character.CommitCharacterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class CharacterDataSourceImpl @Inject constructor(
    private val characterApi: CharacterApi
) : CharacterDataSource {

    override suspend fun postCharacter(characterDTO: CharacterDTO): Flow<BaseResponse<CommitCharacterResponse>> =
        flow {
            val result = characterApi.postCharacter(characterDTO)
            emit(result)
        }.catch { e ->
            Log.e("postCharacter 에러", e.message.toString())
        }

    override suspend fun getCharacter(id: String): Flow<CharacterResponse> = flow {
        val result = characterApi.getCharacter(id)
        emit(result)
    }.catch { e ->
        Log.e("getCharacter 에러", e.message.toString())
    }

    override suspend fun getAllCharacter(): Flow<CharacterAllResponse> = flow {
        val result = characterApi.getAllCharacter()
        emit(result)
    }.catch { e ->
        Log.e("getAllCharacter 에러", e.message.toString())
    }

    override suspend fun deleteCharacter(id: String): Flow<Response<ResponseBody>> = flow {
        val result = characterApi.deleteCharacter(id)
        emit(result)
    }.catch { e ->
        Log.e("deleteCharacter 에러", e.message.toString())
    }

    override suspend fun putCharacter(character_id: Int, name: String): Flow<BaseResponse<Any>> =
        flow {
            val result = characterApi.putCharacter(character_id, name)
            emit(result)
        }.catch { e ->
            Log.e("deleteCharacter 에러", e.message.toString())
        }

    override suspend fun postIncreaseActivityPoint(
        character_id: Int,
        activityPoint: Int
    ): Flow<BaseResponse<Any>> = flow {
        val result = characterApi.postIncreaseActivityPoint(character_id, activityPoint)
        emit(result)
    }.catch { e ->
        Log.e("postIncreaseActivityPoint 에러", e.message.toString())
    }

    override suspend fun postDecreaseActivityPoint(
        character_id: Int,
        activityPoint: Int
    ): Flow<BaseResponse<Any>> = flow {
        val result = characterApi.postDecreaseActivityPoint(character_id, activityPoint)
        emit(result)
    }.catch { e ->
        Log.e("postDecreaseActivityPoint 에러", e.message.toString())
    }

}