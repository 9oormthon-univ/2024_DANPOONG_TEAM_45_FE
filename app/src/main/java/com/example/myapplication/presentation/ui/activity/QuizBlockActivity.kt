package com.example.myapplication.presentation.ui.activity

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuizBinding
import com.example.myapplication.databinding.ActivityQuizBlockBinding
import com.example.myapplication.presentation.base.BaseActivity

class QuizBlockActivity : BaseActivity<ActivityQuizBlockBinding>(R.layout.activity_quiz_block) {

    private lateinit var navController: NavController
    override fun setLayout() {
        setNavController()
    }

    //네비게이션 컨트롤러 세팅
    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityQuizBlockFcv.id) as NavHostFragment
        navController = navHostFragment.navController
    }

}