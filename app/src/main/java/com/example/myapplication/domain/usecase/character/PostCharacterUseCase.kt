package com.example.myapplication.domain.usecase.character

import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.character.CommitCharacterResponse
import com.example.myapplication.domain.repository.character.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(characterDTO: CharacterDTO): Flow<BaseResponse<CommitCharacterResponse>> =
        characterRepository.postCharacter(characterDTO)
}