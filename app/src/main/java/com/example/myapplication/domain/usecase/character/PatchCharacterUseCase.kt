package com.example.myapplication.domain.usecase.character

import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.response.character.CharacterResponse
import com.example.myapplication.domain.repository.character.CharacterRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class PatchCharacterUseCase  @Inject constructor(
    private val characterRepository: CharacterRepository
){
    suspend operator fun invoke(characterDTO: CharacterDTO) : Flow<Response<ResponseBody>> = characterRepository.patchCharacter(characterDTO)
}