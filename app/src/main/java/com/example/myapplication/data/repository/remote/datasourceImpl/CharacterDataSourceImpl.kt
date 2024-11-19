package com.example.myapplication.data.repository.remote.datasourceImpl

import android.util.Log
import com.example.myapplication.data.repository.remote.api.ChapterClearedApi
import com.example.myapplication.data.repository.remote.api.CharacterApi
import com.example.myapplication.data.repository.remote.datasource.remote.CharacterDataSource
import com.example.myapplication.data.repository.remote.datasource.remote.IsChapterClearedDataSource
import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateListResponse
import com.example.myapplication.data.repository.remote.response.chaptercleared.ClearChapterStateResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterAllResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class CharacterDataSourceImpl @Inject constructor(
    private val characterApi: CharacterApi
) : CharacterDataSource {

    override suspend fun postCharacter(characterDTO: CharacterDTO): Flow<Response<ResponseBody>> = flow {
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

    override suspend fun patchCharacter(characterDTO: CharacterDTO): Flow<Response<ResponseBody>> = flow {
        val result = characterApi.patchCharacter(characterDTO)
        emit(result)
    }.catch { e ->
        Log.e("patchCharacter 에러", e.message.toString())
    }

    override suspend fun deleteCharacter(id: String): Flow<Response<ResponseBody>> = flow {
        val result = characterApi.deleteCharacter(id)
        emit(result)
    }.catch { e ->
        Log.e("deleteCharacter 에러", e.message.toString())
    }

}