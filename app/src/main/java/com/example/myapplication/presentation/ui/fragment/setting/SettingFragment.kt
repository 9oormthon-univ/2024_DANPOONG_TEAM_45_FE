package com.example.myapplication.presentation.ui.fragment.setting

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSettingBinding
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.AccountActivity
import com.example.myapplication.presentation.viewmodel.LoginViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.example.myapplication.presentation.widget.extention.loadProfileImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun setLayout() {
        initUser()
    }

    private fun observeLifeCycle(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                loginViewModel.deleteUser.collectLatest {
                    when(it.result.code){
                        200 -> {
                            startActivity(Intent(requireContext(),AccountActivity::class.java))
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    private suspend fun getUserId() = tokenManager.getUserId.first()

    //유저 초기화
    private fun initUser() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                val nickname = tokenManager.getUserNickname.first()
                val profile = tokenManager.getUserProfile.first()
                binding.fragmentSettingProfileIv.loadProfileImage(profile!!)
                binding.fragmentSettingNicknameTv.text = nickname
            }
        }

        binding.exitArrowPoint.setOnClickListener {
            lifecycleScope.launch {
                getUserId()?.let { it1 ->
                    loginViewModel.deleteUser(
                        it1.toInt() + 1
                    )
                    observeLifeCycle()
                }
            }
        }
    }

}