package com.example.myapplication.presentation.ui.fragment.home

import android.content.Intent
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
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

    //뷰를 보여주거나 숨겨주는 함수
    private fun visibleView(view : View, isVisible : Boolean){
        if(isVisible){
            view.visibility = View.VISIBLE
        }
        else{
            view.visibility = View.GONE
        }
    }

    private fun buttonStateCheck() {
        if (binding.fragmentHomeTodayMissionStamp1Iv.isSelected && binding.fragmentHomeTodayMissionStamp2Iv.isSelected) {
            visibleView(binding.fragmentHomeTodayTakeRewordBt,true)
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
        binding.fragmentHomeNicknameBt.setOnClickListener{
            findNavController().popBackStack()
            findNavController().navigate(R.id.questFragment)        }
    }

}