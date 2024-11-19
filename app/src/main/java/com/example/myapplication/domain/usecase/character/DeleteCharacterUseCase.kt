package com.example.myapplication.domain.usecase.character

import com.example.myapplication.domain.repository.character.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class DeleteCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(id: String): Flow<Response<ResponseBody>> = flow {
        characterRepository.deleteCharacter(id)
    }
}