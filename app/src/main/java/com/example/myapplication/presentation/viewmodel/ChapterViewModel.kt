package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import com.example.myapplication.domain.usecase.chapter.GetAllChapterUseCase
import com.example.myapplication.domain.usecase.chapter.GetDistinctChapterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterViewModel @Inject constructor(
    private val getDistinctChapterUseCase: GetDistinctChapterUseCase,
    private val getAllChapterUseCase: GetAllChapterUseCase
) : ViewModel() {

    private val _getDistinctChapter = MutableStateFlow(BaseResponse<DistinctChapterResponse>())
    val getDistinctChapter: StateFlow<BaseResponse<DistinctChapterResponse>> = _getDistinctChapter

    private val _getAllChapter = MutableStateFlow(BaseResponse<AllChapterResponse>())
    val getAllChapter: StateFlow<BaseResponse<AllChapterResponse>> = _getAllChapter

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
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

}