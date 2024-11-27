package com.example.myapplication.presentation.ui.fragment.setting

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSettingBinding
<<<<<<< HEAD
import com.example.myapplication.presentation.ui.activity.GameActivity
import com.example.myapplication.presentation.ui.activity.QuestIntroActivity
=======
import com.example.myapplication.presentation.ui.activity.AdminActivity
>>>>>>> 1e13839181d96487cfd0a5d1bea7d2ad8154ea01
import com.example.myapplication.presentation.ui.activity.QuizBlockActivity
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
        initUser()
    }

    //유저 초기화
    private fun initUser() {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.CREATED) {
//                val nickname = tokenManager.getUserNickname.first()
//                val profile = tokenManager.getUserProfile.first()
//                binding.fragmentSettingProfileIv.loadProfileImage(profile!!)
//                binding.fragmentSettingNicknameTv.text = nickname
//            }
//        }

        //임시 버튼
        binding.fragmentSettingNotificationTv.setOnClickListener{
            val intent = Intent(requireContext(), QuestIntroActivity::class.java)
            intent.putExtra("game id", 2)
            startActivity(intent)

        }
        // 임시 버튼
        binding.fragmentQuestionTv.setOnClickListener{
            startActivity(Intent(requireActivity(),AdminActivity::class.java))
        }
    }

}