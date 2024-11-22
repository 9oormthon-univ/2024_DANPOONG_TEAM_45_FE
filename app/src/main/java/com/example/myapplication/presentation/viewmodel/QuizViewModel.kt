package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.request.quiz.QuizDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quiz.DistinctQuizResponse
import com.example.myapplication.domain.usecase.quiz.DeleteQuizUseCase
import com.example.myapplication.domain.usecase.quiz.GetDistinctQuizUseCase
import com.example.myapplication.domain.usecase.quiz.PostCreateQuizUseCase
import com.example.myapplication.domain.usecase.quizcleared.PostQuizClearedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val postCreateQuizUseCase: PostCreateQuizUseCase,
    private val deleteQuizUseCase: DeleteQuizUseCase,
    private val quizDistinctQuizUseCase: GetDistinctQuizUseCase,
    private val quizClearedUseCase: PostQuizClearedUseCase
) : ViewModel() {
    private val _postCreateQuiz = MutableStateFlow(BaseResponse<Any>())
    val postCreateQuiz: StateFlow<BaseResponse<Any>> = _postCreateQuiz

    private val _deleteQuiz = MutableStateFlow(BaseResponse<Any>())
    val deleteQuiz: StateFlow<BaseResponse<Any>> = _deleteQuiz

    private val _quizDistinct = MutableStateFlow(BaseResponse<DistinctQuizResponse>())
    val quizDistinct: StateFlow<BaseResponse<DistinctQuizResponse>> = _quizDistinct

    private val _quizClear = MutableStateFlow(BaseResponse<Any>())
    val quizClear: StateFlow<BaseResponse<Any>> = _quizClear

    //퀴즈 생성 - 관리자
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

    //퀴즈 삭제 - 관리자
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

    //퀴즈 조회
    fun quizDistinct(quizId: Int) {
        viewModelScope.launch {
            try {
                quizDistinctQuizUseCase(quizId).collect {
                    _quizDistinct.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "postKakaoLogin")
            }
        }
    }

    //완료 퀴즈 등록
    fun postQuizClear(quizId: Int) {
        viewModelScope.launch {
            try {
                quizClearedUseCase(quizId).collect {
                    _quizClear.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "postKakaoLogin")
            }
        }
    }
}