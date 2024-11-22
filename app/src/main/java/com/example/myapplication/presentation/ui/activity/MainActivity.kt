package com.example.myapplication.presentation.ui.activity

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.viewmodel.ChapterViewModel
import com.example.myapplication.presentation.viewmodel.CharacterViewModel
import com.example.myapplication.presentation.viewmodel.DifficultyViewModel
import com.example.myapplication.presentation.viewmodel.EduViewModel
import com.example.myapplication.presentation.viewmodel.HomeViewModel
import com.example.myapplication.presentation.viewmodel.QuizViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var eduViewModel: EduViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var chapterViewModel: ChapterViewModel
    private lateinit var quizVieModel: QuizViewModel
    private lateinit var difficultyViewModel: DifficultyViewModel

    override fun setLayout() {
        initViewModel()
        setBottomNavigation()
        observeLifeCycle()
    }

    private fun initViewModel() {
        eduViewModel = ViewModelProvider(this)[EduViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        characterViewModel = ViewModelProvider(this)[CharacterViewModel::class.java]
        chapterViewModel = ViewModelProvider(this)[ChapterViewModel::class.java]
        quizVieModel = ViewModelProvider(this)[QuizViewModel::class.java]
        difficultyViewModel = ViewModelProvider(this)[DifficultyViewModel::class.java]
//        quizVieModel.postCreateQuiz(
//            QuizDto(
//                title = "무무가 가야할 방향은\n" +
//                        "어디일까요?",
//                message = "무무가 앞으로 가야 할 방향을 헷갈리고 있어요",
//                hint = "",
//                chapterId = 23,
//                level = 1
//            )
//        )
//        quizVieModel.postCreateQuiz(
//            QuizDto(
//                title = "맛있는 라면은 어떤 순서로 \n" +
//                        "요리 해야할까요?",
//                message = "무무가 순서를 잘 기억할 수 있도록 도와주세요!",
//                hint = "",
//                chapterId = 23,
//                level = 1
//            )
//        )
//        quizVieModel.postCreateQuiz(
//            QuizDto(
//                title = "알고리즘이란 무엇일까요?",
//                message = "아래 4가지 선택지 중 하나를 고르세요.",
//                hint = "",
//                chapterId = 23,
//                level = 1
//            )
//        )
    }

    //바텀 네비게이션 세팅
    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityMainFcv.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.activityMainBnv.setupWithNavController(navController)
    }

    private fun observeLifeCycle() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                eduViewModel.getAllQuiz.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                eduViewModel.getDistinctQuiz.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                homeViewModel.getAllHome.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                characterViewModel.postCharacter.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                chapterViewModel.getDistinctChapter.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                chapterViewModel.getAllChapter.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }
    }

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onDestroy() {
        super.onDestroy()
        runBlocking {
            val hd = tokenManager.getUserId.toString()
            tokenManager.deleteHome()
            homeViewModel.deleteHomeId(hd)
        }
    }

}