package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.base.BaseLoadingState
import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.data.repository.remote.request.login.UserListDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.login.LogInKakaoResponse
import com.example.myapplication.domain.usecase.login.CheckTrainingUseCase
import com.example.myapplication.domain.usecase.login.DeleteUserUseCase
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
    private val getUserAllUseCase: GetUserAllUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {
    private val _kakaoLogin = MutableStateFlow(LogInKakaoResponse())
    val kakaoLogin: StateFlow<LogInKakaoResponse> = _kakaoLogin

    private val _training = MutableStateFlow(BaseResponse<Boolean>())
    val training: StateFlow<BaseResponse<Boolean>> = _training

    private val _completeTraining = MutableStateFlow(BaseResponse<Any>())
    val completeTraining: StateFlow<BaseResponse<Any>> = _completeTraining

    private val _getUserAll = MutableStateFlow(BaseResponse<UserListDTO>())
    val getUserAll: StateFlow<BaseResponse<UserListDTO>> = _getUserAll

    private val _deleteUser = MutableStateFlow(BaseResponse<Any>())
    val deleteUser: StateFlow<BaseResponse<Any>> = _deleteUser

    fun postKakaoLogin(logInKakaoDto: LogInKakaoDto) {
        viewModelScope.launch {
            try {
                // 로딩 상태로 초기화
                _kakaoLogin.value = _kakaoLogin.value.copy(state = BaseLoadingState.LOADING)

                postKakaoLoginUseCase(logInKakaoDto).collect { response ->
                    when (response.result.code) {
                        200 -> {
                            // 성공 상태로 업데이트
                            _kakaoLogin.value = response.copy(state = BaseLoadingState.SUCCESS)
                        }

                        else -> {
                            // 실패 상태로 업데이트
                            _kakaoLogin.value =
                                _kakaoLogin.value.copy(state = BaseLoadingState.ERROR)
                            Log.e("postKakaoLogin", "응답 실패: ${response.result.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("postKakaoLogin", "로그인 실패: ${e.message}", e)
                // 에러 상태로 업데이트
                _kakaoLogin.value = _kakaoLogin.value.copy(state = BaseLoadingState.ERROR)
            }
        }
    }

    fun getTraining() {
        viewModelScope.launch {
            try {
                getTrainingUseCase().collect { response ->
                    _training.value = BaseResponse(
                        result = response.result,
                        payload = response.payload,
                        status = BaseLoadingState.SUCCESS // 상태를 업데이트한 새로운 객체 생성
                    )
                }
            } catch (e: Exception) {
                Log.e("실패", "getTraining", e)
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

    fun deleteUser(user_id: Int) {
        viewModelScope.launch {
            try {
                deleteUserUseCase(user_id).collect {
                    _deleteUser.value = it
                }
            } catch (e: Exception) {
                Log.e("실패", "postKakaoLogin")
            }
        }
    }
}