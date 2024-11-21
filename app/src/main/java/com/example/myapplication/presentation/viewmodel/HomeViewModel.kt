package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.request.home.PatchHomeDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import com.example.myapplication.data.repository.remote.response.home.HomeAllList
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
class HomeViewModel @Inject constructor(
    private val deleteHomeIdUseCase: DeleteHomeIdUseCase,
    private val getAllHomeUseCase: GetAllHomeUseCase,
    private val getDistinctHomeUseCase: GetDistinctHomeUseCase,
    private val patchHomeEditUseCase: PatchHomeEditUseCase,
    private val postHomeUseCase: PostHomeUseCase
) : ViewModel() {
    private val _deleteHome = MutableStateFlow(BaseResponse<Any>())
    val deleteHome: StateFlow<BaseResponse<Any>> = _deleteHome

    private val _getAllHome = MutableStateFlow(HomeAllList())
    val getAllHome: StateFlow<HomeAllList> = _getAllHome

    private val _getDistinctHome = MutableStateFlow(BaseResponse<DistinctHomeIdResponse>())
    val getDistinctHome: StateFlow<BaseResponse<DistinctHomeIdResponse>> = _getDistinctHome


    private val _patchHome = MutableStateFlow(BaseResponse<Any>())
    val patchHome: StateFlow<BaseResponse<Any>> = _patchHome


    private val _postHome = MutableStateFlow(BaseResponse<Any>())
    val postHome: StateFlow<BaseResponse<Any>> = _postHome


    fun deleteHomeId(home_id: String) {
        viewModelScope.launch {
            try {
                deleteHomeIdUseCase(home_id).collect {
                    _deleteHome.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun getAllHome() {
        viewModelScope.launch {
            try {
                getAllHomeUseCase().collect {
                    _getAllHome.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun getDistinctHome() {
        viewModelScope.launch {
            try {
                getDistinctHomeUseCase().collect {
                    _getDistinctHome.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun patchHome(
        home_id: String,
        patchHomeDTO: PatchHomeDTO
    ) {
        viewModelScope.launch {
            try {
                patchHomeEditUseCase(home_id,patchHomeDTO).collect {
                    _patchHome.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun postHome() {
        viewModelScope.launch {
            try {
                postHomeUseCase().collect {
                    _postHome.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }
}