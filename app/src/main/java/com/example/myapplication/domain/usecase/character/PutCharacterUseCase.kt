package com.example.myapplication.domain.usecase.character

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.domain.repository.character.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PutCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(character_id : Int, name : String): Flow<BaseResponse<Any>> = flow {
        characterRepository.putCharacter(character_id,name)
    }
}