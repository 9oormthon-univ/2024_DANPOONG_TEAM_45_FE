package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterRandomListResponse
import com.example.myapplication.data.repository.remote.response.character.CharacterRandomResponse
import com.example.myapplication.data.repository.remote.response.character.CommitCharacterResponse
import com.example.myapplication.domain.usecase.character.GetGuideBookUseCase
import com.example.myapplication.domain.usecase.character.GetRandomCactusUseCase
import com.example.myapplication.domain.usecase.character.PostCharacterUseCase
import com.example.myapplication.domain.usecase.character.PostDecreaseActivityPointUseCase
import com.example.myapplication.domain.usecase.character.PostIncreaseActivityPointUseCase
import com.example.myapplication.domain.usecase.character.PutCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val postCharacterUseCase: PostCharacterUseCase,
    private val postDecreaseActivityPointUseCase: PostDecreaseActivityPointUseCase,
    private val postIncreaseActivityPointUseCase: PostIncreaseActivityPointUseCase,
    private val putCharacterUseCase: PutCharacterUseCase,
    private val getRandomCactusUseCase: GetRandomCactusUseCase,
    private val getGuideBookUseCase: GetGuideBookUseCase
) : ViewModel() {

    private val _postCharacter = MutableStateFlow(BaseResponse<CommitCharacterResponse>())
    val postCharacter: StateFlow<BaseResponse<CommitCharacterResponse>> = _postCharacter

    private val _postDecreaseActivity = MutableStateFlow(BaseResponse<Any>())
    val postDecreaseActivity: StateFlow<BaseResponse<Any>> = _postDecreaseActivity

    private val _postIncreaseActivity = MutableStateFlow(BaseResponse<Any>())
    val postIncreaseActivity: StateFlow<BaseResponse<Any>> = _postIncreaseActivity

    private val _putCharacter = MutableStateFlow(BaseResponse<Any>())
    val putCharacter: StateFlow<BaseResponse<Any>> = _putCharacter

    private val _getRandomCactus = MutableStateFlow(BaseResponse<CharacterRandomResponse>())
    val getRandomCactus: StateFlow<BaseResponse<CharacterRandomResponse>> = _getRandomCactus

    private val _getGuideBook = MutableStateFlow(BaseResponse<CharacterRandomListResponse>())
    val getGuideBook: StateFlow<BaseResponse<CharacterRandomListResponse>> = _getGuideBook

    fun postCharacter(characterDTO: CharacterDTO) {
        viewModelScope.launch {
            try {
                postCharacterUseCase(characterDTO).collect {
                    _postCharacter.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun postDecreaseActivity(character_id: Int, activityPoint: Int) {
        viewModelScope.launch {
            try {
                postDecreaseActivityPointUseCase(character_id, activityPoint).collect {
                    _postDecreaseActivity.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun postIncreaseActivity(character_id: Int, activityPoint: Int) {
        viewModelScope.launch {
            try {
                postIncreaseActivityPointUseCase(character_id, activityPoint).collect {
                    _postIncreaseActivity.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun putCharacter(character_id: Int, name: String) {
        viewModelScope.launch {
            try {
                putCharacterUseCase(character_id, name).collect {
                    _putCharacter.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun getRandomCactus() {
        viewModelScope.launch {
            try {
                getRandomCactusUseCase().collect {
                    _getRandomCactus.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun getGuideBook() {
        viewModelScope.launch {
            try {
                getGuideBookUseCase().collect {
                    _getGuideBook.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }


}