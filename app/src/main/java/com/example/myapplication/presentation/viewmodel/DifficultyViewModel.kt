package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.domain.usecase.difficulty.DeleteDifficultyLevel
import com.example.myapplication.domain.usecase.difficulty.PostDifficultyLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DifficultyViewModel @Inject constructor(
    private val postDifficultyLevel: PostDifficultyLevel,
    private val deleteDifficultyLevel: DeleteDifficultyLevel
) : ViewModel() {
    private val _postDifficulty = MutableStateFlow(BaseResponse<Any>())
    val postDifficulty: StateFlow<BaseResponse<Any>> = _postDifficulty

    private val _deleteDifficulty = MutableStateFlow(BaseResponse<Any>())
    val deleteDifficulty: StateFlow<BaseResponse<Any>> = _deleteDifficulty

    fun postCreateDifficulty(level: String) {
        viewModelScope.launch {
            try {
                postDifficultyLevel(level).collect {
                    _postDifficulty.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "postKakaoLogin")
            }
        }
    }

    fun deleteDifficulty(level: String) {
        viewModelScope.launch {
            try {
                deleteDifficultyLevel(level).collect {
                    _deleteDifficulty.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "postKakaoLogin")
            }
        }
    }
}