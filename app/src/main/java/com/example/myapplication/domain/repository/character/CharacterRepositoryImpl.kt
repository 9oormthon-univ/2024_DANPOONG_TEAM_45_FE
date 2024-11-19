package com.example.myapplication.domain.repository.character

import com.example.myapplication.data.repository.remote.datasource.remote.CharacterDataSource
import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.response.character.CharacterAllResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val characterDataSource: CharacterDataSource
) : CharacterRepository{
    override suspend fun postCharacter(characterDTO: CharacterDTO): Flow<Response<ResponseBody>> = characterDataSource.postCharacter(characterDTO)

    override suspend fun getCharacter(id: String): Flow<CharacterResponse> = characterDataSource.getCharacter(id)

    override suspend fun getAllCharacter(): Flow<CharacterAllResponse> = characterDataSource.getAllCharacter()

    override suspend fun patchCharacter(characterDTO: CharacterDTO): Flow<Response<ResponseBody>> = characterDataSource.patchCharacter(characterDTO)

    override suspend fun deleteCharacter(id: String): Flow<Response<ResponseBody>> = characterDataSource.deleteCharacter(id)
}