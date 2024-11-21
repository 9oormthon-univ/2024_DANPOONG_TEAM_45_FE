package com.example.myapplication.domain.usecase.character

import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.domain.repository.character.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostDecreaseActivityPointUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(character_id: Int, activityPoint: Int): Flow<BaseResponse<Any>> =
        characterRepository.postDecreaseActivityPoint(character_id, activityPoint)
}