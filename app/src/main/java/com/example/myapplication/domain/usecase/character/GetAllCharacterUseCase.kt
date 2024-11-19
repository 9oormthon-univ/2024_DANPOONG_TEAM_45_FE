package com.example.myapplication.domain.usecase.character

import com.example.myapplication.data.repository.remote.response.character.CharacterAllResponse
import com.example.myapplication.domain.repository.character.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(): Flow<CharacterAllResponse> =
        characterRepository.getAllCharacter()
}