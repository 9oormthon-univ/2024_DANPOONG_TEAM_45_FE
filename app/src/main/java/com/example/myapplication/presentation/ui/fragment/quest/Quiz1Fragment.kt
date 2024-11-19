package com.example.myapplication.presentation.ui.fragment.quest

import android.view.View
import com.example.codingland.presenter.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCharacterMoveBinding
import com.example.myapplication.presentation.ui.activity.QuizActivity
import com.example.myapplication.presentation.widget.extention.loadCropRoundedSquareImage

class Quiz1Fragment : BaseFragment<ItemCharacterMoveBinding>(R.layout.item_character_move) {
    override fun setLayout() {
        checkButtonState()
    }

    private fun checkButtonState() {
        binding.itemCharacterMoveClickLeftBt.setOnClickListener {
            buttonState(it, binding.itemCharacterMoveClickRightBt)
            confirmCorrectQuestion()
            buttonSet(true)
        }
        binding.itemCharacterMoveClickRightBt.setOnClickListener {
            buttonState(it, binding.itemCharacterMoveClickLeftBt)
            confirmCorrectQuestion()
            buttonSet(true)
        }
    }

    //버튼 상태 변경
    private fun buttonState(view1: View, view2: View) {
        view1.isSelected = true
        view2.isSelected = false
    }

    private fun confirmCorrectQuestion() {
        val av = requireActivity() as QuizActivity
        if (binding.itemCharacterMoveClickRightBt.isSelected) {
            av.setReplaceLevelState(true)
        } else {
            av.setReplaceLevelState(false)
        }
    }

    private fun buttonSet(isState: Boolean) {
        val av = requireActivity() as QuizActivity
        av.onButtonState(isState)
    }

    override fun onDestroy() {
        super.onDestroy()
        buttonSet(false)
        val av = requireActivity() as QuizActivity
        av.setReplaceLevelState(false)
    }
}