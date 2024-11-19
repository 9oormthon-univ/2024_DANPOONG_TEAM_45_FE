package com.example.myapplication.domain.usecase.character

import com.example.myapplication.data.repository.remote.response.character.CharacterResponse
import com.example.myapplication.domain.repository.character.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
){
    suspend operator fun invoke(id : String) : Flow<CharacterResponse> = characterRepository.getCharacter(id)
}