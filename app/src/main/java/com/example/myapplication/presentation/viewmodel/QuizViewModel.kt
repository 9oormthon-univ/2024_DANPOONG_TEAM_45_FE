package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.request.quiz.QuizDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.domain.usecase.quiz.DeleteQuizUseCase
import com.example.myapplication.domain.usecase.quiz.PostCreateQuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val postCreateQuizUseCase: PostCreateQuizUseCase,
    private val deleteQuizUseCase: DeleteQuizUseCase
) : ViewModel() {
    private val _postCreateQuiz = MutableStateFlow(BaseResponse<Any>())
    val postCreateQuiz: StateFlow<BaseResponse<Any>> = _postCreateQuiz

    private val _deleteQuiz = MutableStateFlow(BaseResponse<Any>())
    val deleteQuiz: StateFlow<BaseResponse<Any>> = _deleteQuiz

    fun postCreateQuiz(quizDto: QuizDto) {
        viewModelScope.launch {
            try {
                postCreateQuizUseCase(quizDto).collect {
                    _postCreateQuiz.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "postKakaoLogin")
            }
        }
    }

    fun deleteQuiz(quizId: Int) {
        viewModelScope.launch {
            try {
                deleteQuizUseCase(quizId).collect {
                    _deleteQuiz.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "postKakaoLogin")
            }
        }
    }
}