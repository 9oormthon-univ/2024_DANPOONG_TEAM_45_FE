package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.data.repository.remote.request.home.PatchHomeDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.character.CommitCharacterResponse
import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import com.example.myapplication.data.repository.remote.response.home.HomeAllList
import com.example.myapplication.domain.usecase.character.PostCharacterUseCase
import com.example.myapplication.domain.usecase.home.DeleteHomeIdUseCase
import com.example.myapplication.domain.usecase.home.GetAllHomeUseCase
import com.example.myapplication.domain.usecase.home.GetDistinctHomeUseCase
import com.example.myapplication.domain.usecase.home.PatchHomeEditUseCase
import com.example.myapplication.domain.usecase.home.PostHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val postCharacterUseCase: PostCharacterUseCase

) : ViewModel() {

    private val _postCharacter = MutableStateFlow(BaseResponse<CommitCharacterResponse>())
    val postCharacter: StateFlow<BaseResponse<CommitCharacterResponse>> = _postCharacter


    fun postCharacter(characterDTO : CharacterDTO) {
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


}