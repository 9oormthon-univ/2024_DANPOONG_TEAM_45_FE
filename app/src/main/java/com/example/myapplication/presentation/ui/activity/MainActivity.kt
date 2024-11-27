package com.example.myapplication.presentation.ui.activity

import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.domain.model.ChatMessage
import com.example.myapplication.domain.model.ChatOwner
import com.example.myapplication.presentation.adapter.AdapterItemClickedListener
import com.example.myapplication.presentation.adapter.ChattingAdapter
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.viewmodel.AiViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {


    @Inject
    lateinit var tokenManager: TokenManager

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