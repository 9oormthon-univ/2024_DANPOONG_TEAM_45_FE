package com.example.myapplication.presentation.ui.activity

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityTutorialBinding
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.ui.fragment.quest.DialogClickListener

class TutorialActivity: BaseActivity<ActivityTutorialBinding>(R.layout.activity_tutorial),
    DialogClickListener {

    private lateinit var navController: NavController
    private var buttonPosition = 1
    private var levelCorrect = false

    override fun setLayout() {
        setNavController()
        nextFragment()
    }

    //네비게이션 컨트롤러 세팅
    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityTutorialFcv.id) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun nextFragmentWithIndex() {
        when (buttonPosition) {
            1 -> {
                navController.navigate(
                    R.id.tutorialFragment2, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.tutorialFragment1, true)  // 시작 프래그먼트 제거
                        .setLaunchSingleTop(true)
                        .build()
                )
            }

//            2 -> {
//                navController.navigate(
//                    R.id.quiz3Fragment, null,
//                    NavOptions.Builder()
//                        .setPopUpTo(R.id.quiz2Fragment, true)  // 이전 프래그먼트 제거
//                        .setLaunchSingleTop(true)
//                        .build()
//                )
//            }
//
//            3 -> {
//                quizViewModel.postQuizClear(1)
//            }
        }
    }

    //버튼 이동
    private fun nextFragment() {
        binding.activityTutorialConstraint.setOnClickListener {
            nextFragmentWithIndex()
        }
    }

    fun setReplaceLevelState(levelState: Boolean) {
        levelCorrect = levelState
    }

    override fun onClickNext() {
        nextFragmentWithIndex()
    }

    override fun onClickStop() {
        finish()
    }
}