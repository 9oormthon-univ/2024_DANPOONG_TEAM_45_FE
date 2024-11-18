package com.example.myapplication.presentation.ui.activity

import androidx.navigation.NavController
import com.example.codingland.presenter.base.BaseActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuizBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizActivity : BaseActivity<ActivityQuizBinding>(R.layout.activity_quiz) {
    private lateinit var navController: NavController


    override fun setLayout() {

    }


}