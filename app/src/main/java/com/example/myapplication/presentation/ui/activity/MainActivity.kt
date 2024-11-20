package com.example.myapplication.presentation.ui.activity

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController

    override fun setLayout() {
        setBottomNavigation()
    }
    //바텀 네비게이션 세팅
    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityMainFcv.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.activityMainBnv.setupWithNavController(navController)
    }
}