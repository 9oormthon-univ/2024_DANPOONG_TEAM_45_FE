package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import com.example.myapplication.domain.usecase.chapter.DeleteChapterUseCase
import com.example.myapplication.domain.usecase.chapter.GetAllChapterUseCase
import com.example.myapplication.domain.usecase.chapter.GetDistinctChapterUseCase
import com.example.myapplication.domain.usecase.chapter.PostCreateChapterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val getDistinctChapterUseCase: GetDistinctChapterUseCase,
    private val getAllChapterUseCase: GetAllChapterUseCase,
    private val postCreateChapterUseCase: PostCreateChapterUseCase,
    private val deleteChapterUseCase: DeleteChapterUseCase,
) : ViewModel() {

    private val _postCreateChapter = MutableStateFlow(BaseResponse<Any>())
    val postCreateChapter: StateFlow<BaseResponse<Any>> = _postCreateChapter

    private val _getDistinctChapter = MutableStateFlow(BaseResponse<DistinctChapterResponse>())
    val getDistinctChapter: StateFlow<BaseResponse<DistinctChapterResponse>> = _getDistinctChapter

    private val _getAllChapter = MutableStateFlow(BaseResponse<AllChapterResponse>())
    val getAllChapter: StateFlow<BaseResponse<AllChapterResponse>> = _getAllChapter

    private val _deleteChapter = MutableStateFlow(BaseResponse<Any>())
    val deleteChapter: StateFlow<BaseResponse<Any>> = _deleteChapter

    fun getDistinctChapter(chapter_id: Int) {
        viewModelScope.launch {
            try {
                getDistinctChapterUseCase(chapter_id).collect {
                    _getDistinctChapter.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun getAllChapter() {
        viewModelScope.launch {
            try {
                getAllChapterUseCase().collect {
                    _getAllChapter.value = it
                    Log.d("okhttp viewModel","${_getAllChapter.value}")
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun postCreateChapter(name: String) {
        viewModelScope.launch {
            try {
                postCreateChapterUseCase(RegisterChapterDto(name = name)).collect {
                    _postCreateChapter.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun deleteChapter(chapterId : String) {
        viewModelScope.launch {
            try {
                deleteChapterUseCase(chapterId).collect {
                    _deleteChapter.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

}