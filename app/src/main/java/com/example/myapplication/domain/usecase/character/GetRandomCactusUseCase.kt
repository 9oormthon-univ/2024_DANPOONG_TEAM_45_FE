package com.example.myapplication.domain.usecase.character

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterRandomResponse
import com.example.myapplication.domain.repository.character.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetRandomCactusUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(): Flow<BaseResponse<CharacterRandomResponse>> =
        characterRepository.getRandomCactus()
}