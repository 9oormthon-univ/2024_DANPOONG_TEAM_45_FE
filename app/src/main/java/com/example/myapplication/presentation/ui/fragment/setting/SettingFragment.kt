package com.example.myapplication.presentation.ui.fragment.setting

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.codingland.presenter.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSettingBinding
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.example.myapplication.presentation.widget.extention.loadProfileImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun setLayout() {
    }

}