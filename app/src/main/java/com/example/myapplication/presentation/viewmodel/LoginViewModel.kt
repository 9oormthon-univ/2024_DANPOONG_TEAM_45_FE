package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import com.example.myapplication.domain.usecase.login.PostKakaoLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postKakaoLoginUseCase: PostKakaoLoginUseCase
) : ViewModel() {
    private val _kakaoLogin = MutableStateFlow(LogInKakaoResponse())
    val kakaoLogin: StateFlow<LogInKakaoResponse> = _kakaoLogin

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
}