package com.example.myapplication.presentation.ui.activity

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : BaseActivity<ActivityAccountBinding>(R.layout.activity_account) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }

    override fun setLayout() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityLoginFcv.id) as NavHostFragment
        navController = navHostFragment.navController
    }

}