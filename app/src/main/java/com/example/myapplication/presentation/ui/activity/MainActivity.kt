package com.example.myapplication.presentation.ui.activity

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.presentation.viewmodel.EduViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController
    private lateinit var eduViewModel: EduViewModel
    override fun setLayout() {
        initViewModel()
        setBottomNavigation()
        observeLifeCycle()
    }

    private fun initViewModel(){
        eduViewModel = ViewModelProvider(this)[EduViewModel::class.java]
        eduViewModel.getAllQuiz()
        eduViewModel.getDistinctQuiz(1)
    }

    //바텀 네비게이션 세팅
    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityMainFcv.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.activityMainBnv.setupWithNavController(navController)
    }

    private fun observeLifeCycle(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                eduViewModel.getAllQuiz.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                eduViewModel.getDistinctQuiz.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }
    }

}