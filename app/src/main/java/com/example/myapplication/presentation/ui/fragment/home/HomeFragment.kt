package com.example.myapplication.presentation.ui.fragment.home

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.presentation.ui.activity.PotionMysteryActivity

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun setLayout() {
        stateManage()
        onClickBtn()
    }

    private fun stateManage() {
        binding.fragmentHomeTodayMissionStamp1Iv.setOnClickListener {
            it.isSelected = true
            buttonStateCheck()
        }
        binding.fragmentHomeTodayMissionStamp2Iv.setOnClickListener {
            it.isSelected = true
            buttonStateCheck()
        }
    }

    private fun buttonStateCheck() {
        if (binding.fragmentHomeTodayMissionStamp1Iv.isSelected && binding.fragmentHomeTodayMissionStamp2Iv.isSelected) {
            binding.fragmentHomeTodayTakeRewordBt.isSelected = true
        }
    }

    private fun onClickBtn(){
        binding.fragmentHomeTodayTakeRewordBt.setOnClickListener{
            if(it.isSelected){
                startActivity(Intent(requireContext(), PotionMysteryActivity::class.java))
            }
        }
        binding.activityMainSettingIv.setOnClickListener {
            findNavController().navigate(R.id.settingFragment)
        }
    }

}