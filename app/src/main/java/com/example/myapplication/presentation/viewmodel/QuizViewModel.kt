package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.data.repository.remote.request.quiz.QuizDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.quiz.DailyCompleteResponse
import com.example.myapplication.data.repository.remote.response.quiz.DistinctQuizResponse
import com.example.myapplication.data.repository.remote.response.quizcleared.ClearStateListResponse
import com.example.myapplication.domain.usecase.quiz.DeleteQuizUseCase
import com.example.myapplication.domain.usecase.quiz.GetCompleteQuizUseCase
import com.example.myapplication.domain.usecase.quiz.GetDistinctQuizUseCase
import com.example.myapplication.domain.usecase.quiz.PostCompleteQuizUseCase
import com.example.myapplication.domain.usecase.quiz.PostCreateQuizUseCase
import com.example.myapplication.domain.usecase.quizcleared.GetQuizAllUseCase
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
    private val quizClearedUseCase: PostQuizClearedUseCase,
    private val getQuizAllUseCase: GetQuizAllUseCase,
    private val getCompleteQuizUseCase: GetCompleteQuizUseCase,
    private val postCompleteQuizUseCase: PostCompleteQuizUseCase
) : ViewModel() {
    private val _postCreateQuiz = MutableStateFlow(BaseResponse<Any>())
    val postCreateQuiz: StateFlow<BaseResponse<Any>> = _postCreateQuiz

    private val _deleteQuiz = MutableStateFlow(BaseResponse<Any>())
    val deleteQuiz: StateFlow<BaseResponse<Any>> = _deleteQuiz

    private val _quizDistinct = MutableStateFlow(BaseResponse<DistinctQuizResponse>())
    val quizDistinct: StateFlow<BaseResponse<DistinctQuizResponse>> = _quizDistinct

    private val _quizClear = MutableStateFlow(BaseResponse<Any>())
    val quizClear: StateFlow<BaseResponse<Any>> = _quizClear

    private val _quizAllClear = MutableStateFlow(BaseResponse<ClearStateListResponse>())
    val quizAllClear: StateFlow<BaseResponse<ClearStateListResponse>> = _quizAllClear

    private val _moveWay = MutableLiveData<MutableList<Int>>()
    val moveWay: LiveData<MutableList<Int>> get() = _moveWay

    private val _quizComplete = MutableStateFlow(BaseResponse<DailyCompleteResponse>())
    val quizComplete : StateFlow<BaseResponse<DailyCompleteResponse>> = _quizComplete

    private val _postQuizComplete = MutableStateFlow(BaseResponse<Any>())
    val postQuizComplete : StateFlow<BaseResponse<Any>> = _postQuizComplete

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

    //완료 퀴즈 등록
    fun getQuizAll() {
        viewModelScope.launch {
            try {
                getQuizAllUseCase().collect {
                    _quizAllClear.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "getQuizAll")
            }
        }
    }

    fun postCompleteQuiz(){
        viewModelScope.launch {
            try{
                postCompleteQuizUseCase().collect{
                    _postQuizComplete.value = it
                }
            }catch (e : Exception){
                Log.e("실패", "getCompleteQuiz")
            }
        }
    }

    fun getCompleteQuiz(){
        viewModelScope.launch {
            try{
                getCompleteQuizUseCase().collect{
                    _quizComplete.value = it
                }
            }catch (e : Exception){
                Log.e("실패", "getCompleteQuiz")
            }
        }
    }

    fun setMoveWay(newMoveWay: MutableList<Int>) {
        _moveWay.value = newMoveWay
    }

    fun updateMoveWay(index: Int, value: Int) {
        val currentList = _moveWay.value ?: mutableListOf()
        currentList[index] = value
        _moveWay.value = currentList
    }

    fun checkSuccess1(): Boolean {
        val correctBlockOrder = listOf(R.string.game_wake, R.string.game_wash, R.string.game_breakfast, R.string.game_practice)
        val successCnt = _moveWay.value?.zip(correctBlockOrder) { a, b -> a == b }?.count { it } ?: 0
        Log.d("success cnt", successCnt.toString())

        return successCnt == correctBlockOrder.size
    }

    fun checkSuccess2(): Boolean {
        val correctBlockOrder = listOf(R.string.game_wave, R.string.game_wave, R.string.game_repeat, R.string.game_wave)
        val successCnt = _moveWay.value?.zip(correctBlockOrder) { a, b -> a == b }?.count { it } ?: 0
        Log.d("success cnt", successCnt.toString())

        return successCnt == correctBlockOrder.size
    }
}