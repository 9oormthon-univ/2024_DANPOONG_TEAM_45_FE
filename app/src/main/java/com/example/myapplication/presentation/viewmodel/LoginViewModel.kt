package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.request.login.UserListDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import com.example.myapplication.domain.usecase.login.CheckTrainingUseCase
import com.example.myapplication.domain.usecase.login.GetCompleteTrainingUseCase
import com.example.myapplication.domain.usecase.login.GetUserAllUseCase
import com.example.myapplication.domain.usecase.login.PostKakaoLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postKakaoLoginUseCase: PostKakaoLoginUseCase,
    private val getCompleteTrainingUseCase: GetCompleteTrainingUseCase,
    private val getTrainingUseCase: CheckTrainingUseCase,
    private val getUserAllUseCase: GetUserAllUseCase
) : ViewModel() {
    private val _kakaoLogin = MutableStateFlow(LogInKakaoResponse())
    val kakaoLogin: StateFlow<LogInKakaoResponse> = _kakaoLogin

    private val _training = MutableStateFlow(BaseResponse<Boolean>())
    val training: StateFlow<BaseResponse<Boolean>> = _training

    private val _completeTraining = MutableStateFlow(BaseResponse<Any>())
    val completeTraining: StateFlow<BaseResponse<Any>> = _completeTraining

    private val _getUserAll = MutableStateFlow(BaseResponse<UserListDTO>())
    val getUserAll: StateFlow<BaseResponse<UserListDTO>> = _getUserAll

    fun postKakaoLogin(logInKakaoDto: LogInKakaoDto) {
        viewModelScope.launch {
            try {
                postKakaoLoginUseCase(logInKakaoDto).collect {
                    _kakaoLogin.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "postKakaoLogin")
            }
        }
    }

    fun getTraining() {
        viewModelScope.launch {
            try {
                getTrainingUseCase().collect {
                    _training.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "postKakaoLogin")
            }
        }
    }

    fun getCompleteTraining() {
        viewModelScope.launch {
            try {
                getCompleteTrainingUseCase().collect {
                    _completeTraining.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "postKakaoLogin")
            }
        }
    }

    fun getUserAll() {
        viewModelScope.launch {
            try {
                getUserAllUseCase().collect {
                    _getUserAll.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "postKakaoLogin")
            }
        }
    }
}