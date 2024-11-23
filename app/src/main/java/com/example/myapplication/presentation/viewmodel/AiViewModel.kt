package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.domain.usecase.ai.PostAiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val aiUseCase: PostAiUseCase
) : ViewModel() {

    private val _ai = MutableStateFlow(BaseResponse<String>())
    val ai: StateFlow<BaseResponse<String>> = _ai


    fun postAi(keyWord: String) {
        viewModelScope.launch {
            try {
                aiUseCase(keyWord).collect {
                    _ai.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }
}