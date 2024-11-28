package com.example.myapplication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.local.ChapterDTO
import com.example.myapplication.data.repository.local.QuizDTO
import com.example.myapplication.data.repository.local.dao.ChapterRepository
import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import com.example.myapplication.domain.usecase.chapter.DeleteChapterUseCase
import com.example.myapplication.domain.usecase.chapter.GetAllChapterUseCase
import com.example.myapplication.domain.usecase.chapter.GetDistinctChapterUseCase
import com.example.myapplication.domain.usecase.chapter.PatchChapterUseCase
import com.example.myapplication.domain.usecase.chapter.PostCreateChapterUseCase
import com.example.myapplication.domain.usecase.chapter.RewardUseCase
import com.example.myapplication.domain.usecase.chaptercleared.PostChapterClearedUseCase
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
    private val chapterClearedUseCase: PostChapterClearedUseCase,
    private val rewardUseCase: RewardUseCase,
    private val patchChapterUseCase: PatchChapterUseCase,
    private val repository: ChapterRepository

) : ViewModel() {

    private val _postCreateChapter = MutableStateFlow(BaseResponse<Any>())
    val postCreateChapter: StateFlow<BaseResponse<Any>> = _postCreateChapter

    private val _getDistinctChapter = MutableStateFlow(BaseResponse<DistinctChapterResponse>())
    val getDistinctChapter: StateFlow<BaseResponse<DistinctChapterResponse>> = _getDistinctChapter

    private val _getAllChapter = MutableStateFlow(BaseResponse<AllChapterResponse>())
    val getAllChapter: StateFlow<BaseResponse<AllChapterResponse>> = _getAllChapter

    private val _deleteChapter = MutableStateFlow(BaseResponse<Any>())
    val deleteChapter: StateFlow<BaseResponse<Any>> = _deleteChapter

    private val _patchChapter = MutableStateFlow(BaseResponse<Any>())
    val patchChapter: StateFlow<BaseResponse<Any>> = _patchChapter

    private val _chapterClear = MutableStateFlow(BaseResponse<Any>())
    val chapterClear: StateFlow<BaseResponse<Any>> = _chapterClear

    private val _reward = MutableStateFlow(BaseResponse<Any>())
    val reward: StateFlow<BaseResponse<Any>> = _reward

    private val _chapters = MutableLiveData<List<ChapterDTO>>()
    val chapters: LiveData<List<ChapterDTO>> = _chapters

    // 내부에서 관리하는 MutableStateFlow
    private val _quizzesForChapter = MutableStateFlow<List<QuizDTO>>(emptyList())
    val quizzesForChapter: StateFlow<List<QuizDTO>> = _quizzesForChapter

    fun fetchAndSaveChapters(chapterResponses: List<DistinctChapterResponse>) {
        viewModelScope.launch {
            repository.saveChaptersWithQuizzes(chapterResponses)
            _chapters.value = repository.getAllChapters()
        }
    }

    fun fetchQuizzesForChapter(chapterId: Int) {
        viewModelScope.launch {
            repository.getQuizzesByChapterFlow(chapterId)
                .collect { quizzes ->
                    _quizzesForChapter.value = quizzes
                }
        }
    }

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
                    Log.d("okhttp viewModel", "${_getAllChapter.value}")
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

    fun deleteChapter(chapterId: String) {
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

    //챕터 완료
    fun postChapterClear(chapterId: Int) {
        viewModelScope.launch {
            try {
                chapterClearedUseCase(chapterId).collect {
                    _postCreateChapter.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    //챕터 완료
    fun reward(chapterId: Int) {
        viewModelScope.launch {
            try {
                rewardUseCase(chapterId).collect {
                    _postCreateChapter.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }

    fun patchChapter(chapterId: String, registerChapterDto: RegisterChapterDto) {
        viewModelScope.launch {
            try {
                patchChapterUseCase(chapterId, registerChapterDto).collect {
                    _patchChapter.value = it
                }
            } catch (e: Exception) {
                Log.e("에러", e.message.toString())
            }
        }
    }


}