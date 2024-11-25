package com.example.myapplication.presentation.ui.fragment.quest

import android.view.View
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentQuizBlock1Binding
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.QuizActivity
import com.example.myapplication.presentation.ui.activity.QuizBlockActivity

class QuizBlock1Fragment : BaseFragment<FragmentQuizBlock1Binding>(R.layout.fragment_quiz_block_1) {

    private var isState = false

    override fun setLayout() {
        //블럭 튜토리얼 1번
//        binding.ibGameplayBtn.setOnClickListener {
//            buttonSet(isState)
//        }
    }

//    private fun buttonSet(isState: Boolean) {
//        val av = requireActivity() as QuizBlockActivity
//        av.onStoryState(isState)
//        av.onGameplayState(isState)
//    }
//
//    private fun buttonState(view1: View, view2: View) {
//        view1.isSelected = true
//        view2.isSelected = false
//    }


}