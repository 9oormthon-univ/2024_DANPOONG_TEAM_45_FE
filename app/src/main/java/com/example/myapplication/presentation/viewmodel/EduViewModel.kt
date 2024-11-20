package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quiz.AllQuizResponse
import com.example.myapplication.data.repository.remote.response.quiz.DistinctQuizResponse
import com.example.myapplication.domain.usecase.quiz.GetAllQuizUseCase
import com.example.myapplication.domain.usecase.quiz.GetDistinctQuizUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EduViewModel @Inject constructor(
    private val getAllQuizUseCase: GetAllQuizUseCase,
    private val getDistinctQuizUseCase: GetDistinctQuizUseCase,

) : ViewModel() {
    private val _getAllQuiz = MutableStateFlow(BaseResponse<AllQuizResponse>())
    val getAllQuiz: StateFlow<BaseResponse<AllQuizResponse>> = _getAllQuiz

    private val _getDistinctQuiz = MutableStateFlow(BaseResponse<DistinctQuizResponse>())
    val getDistinctQuiz: StateFlow<BaseResponse<DistinctQuizResponse>> = _getDistinctQuiz

    fun getAllQuiz() {
        viewModelScope.launch {
            try {
                getAllQuizUseCase().collect {
                    _getAllQuiz.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun getDistinctQuiz(quizId : Int) {
        viewModelScope.launch {
            try {
                getDistinctQuizUseCase(quizId).collect {
                    _getDistinctQuiz.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }
}