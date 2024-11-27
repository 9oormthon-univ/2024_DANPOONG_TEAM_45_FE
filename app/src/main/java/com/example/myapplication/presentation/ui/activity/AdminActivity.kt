package com.example.myapplication.presentation.ui.activity

import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.data.repository.remote.request.chapter.RegisterChapterDto
import com.example.myapplication.data.repository.remote.request.quiz.QuizDto
import com.example.myapplication.databinding.ActivityAdminBinding
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.viewmodel.ChapterViewModel
import com.example.myapplication.presentation.viewmodel.DifficultyViewModel
import com.example.myapplication.presentation.viewmodel.LoginViewModel
import com.example.myapplication.presentation.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class AdminActivity : BaseActivity<ActivityAdminBinding>(R.layout.activity_admin) {
    private lateinit var difficultyViewModel: DifficultyViewModel
    private lateinit var chapterViewModel: ChapterViewModel
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var loginViewModel: LoginViewModel

    override fun setLayout() {
        initViewModel()
        setOnClickBtn()
    }

    private fun initViewModel() {
        difficultyViewModel = ViewModelProvider(this)[DifficultyViewModel::class.java]
        chapterViewModel = ViewModelProvider(this)[ChapterViewModel::class.java]
        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private fun setOnClickBtn() {
        with(binding) {
            quizBt.setOnClickListener {
                quizViewModel.postCreateQuiz(
//                    QuizDto(
//                        listOf(),
//                        listOf(),
//                        "NORMAL",
//                        "1단계 : 기초 훈련하기",
//                        "초보 모험가를 위한 기초 훈련!",
//                        chapterEt.text.toString().toInt(),
//                        1,
//                        ""
//                    )

                    QuizDto(
                        listOf(),
                        listOf(),
                        "NORMAL",
                        "2단계 : 모험 준비하기",
                        "본격적으로 모험을 준비해봐요!",
                        chapterEt.text.toString().toInt(),
                        1,
                        ""
                    )

                )
            }
            difficultyBt.setOnClickListener {
//                difficultyViewModel.postCreateDifficulty("1")
            }
            chapterBt.setOnClickListener {
//                chapterViewModel.postCreateChapter(binding.chapterEt.text.toString())
                loginViewModel.deleteUser(4)
            }
        }
    }
}