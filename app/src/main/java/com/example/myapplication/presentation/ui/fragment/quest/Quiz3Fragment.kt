package com.example.myapplication.presentation.ui.fragment.quest

import android.view.View
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemAlgorithmBinding
import com.example.myapplication.databinding.ItemCandyBinding
import com.example.myapplication.presentation.ui.activity.QuizActivity
import com.example.myapplication.presentation.widget.extention.loadCropRoundedSquareImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class Quiz3Fragment :
    BaseFragment<ItemCandyBinding>(R.layout.item_candy) {

    private var selectedNumber = 0

    private lateinit var viewList: List<View>

    override fun setLayout() {
        initList()
        //btnClick()
    }

    private fun initList() {
        viewList = listOf(
//            binding.btnBiginnerProblem3Answer1Off,
//            binding.btnBiginnerProblem3Answer2Off,
//            binding.btnBiginnerProblem3Answer3Off,
//            binding.btnBiginnerProblem3Answer4Off
        )
    }

    private fun toggleVisibility(view: View) {
        view.isSelected = !view.isSelected
    }

//    private fun btnClick() {
//        // 힌트
//        binding.ibBiginnerProblem3HintOff.setOnClickListener {
//            val view = binding.ibBiginnerProblem3HintContent
//            if(view.visibility == View.GONE) {
//                binding.ibBiginnerProblem3HintContent.visibility = View.VISIBLE
//            }
//            else{
//                binding.ibBiginnerProblem3HintContent.visibility = View.GONE
//            }
//        }
//        // 1번
//        binding.btnBiginnerProblem3Answer1Off.setOnClickListener {
//            selectAllAnswers(it)  // 모든 답변을 선택 해제
//            selectedNumber = 1
//            confirmCorrectQuestion()
//            buttonSet(true)
//        }
//        // 2번
//        binding.btnBiginnerProblem3Answer2Off.setOnClickListener {
//            selectAllAnswers(it)  // 모든 답변을 선택 해제
//            selectedNumber = 2
//            confirmCorrectQuestion()
//            buttonSet(true)
//        }
//        // 3번
//        binding.btnBiginnerProblem3Answer3Off.setOnClickListener {
//            selectAllAnswers(it)  // 모든 답변을 선택 해제
//            selectedNumber = 3
//            confirmCorrectQuestion()
//            buttonSet(true)
//        }
//
//        // 4번
//        binding.btnBiginnerProblem3Answer4Off.setOnClickListener {
//            selectAllAnswers(it)  // 모든 답변을 선택 해제
//            selectedNumber = 4
//            confirmCorrectQuestion()
//            buttonSet(true)
//        }
//    }
//
//
//    private fun hintSelected(view: View) {
//        if (binding.ibBiginnerProblem3HintOff.isSelected) {
//            view.visibility = View.VISIBLE
//        } else {
//            view.visibility = View.GONE
//        }
//    }
//
//    private fun selectAllAnswers(view: View) {
//        viewList.map {
//            it.isSelected = view == it
//        }
//    }
//
//    private fun buttonSet(isState: Boolean) {
//        val av = requireActivity() as QuizActivity
//        av.onButtonState(isState)
//    }
//
//    private fun confirmCorrectQuestion() {
//        val av = requireActivity() as QuizActivity
//        if (selectedNumber == 2) {
//            av.setReplaceLevelState(true)
//        } else {
//            av.setReplaceLevelState(false)
//        }
//    }


//    override fun onDestroy() {
//        super.onDestroy()
//        buttonSet(false)
//    }
}