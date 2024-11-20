package com.example.myapplication.presentation.ui.fragment.quest

import android.util.Log
import android.view.View
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemRamenOrderBinding
import com.example.myapplication.presentation.ui.activity.QuizActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Quiz2Fragment : BaseFragment<ItemRamenOrderBinding>(R.layout.item_ramen_order) {

    private lateinit var correctList : List<View>


    private val buttonList = mutableListOf<View>()
    override fun setLayout() {
        correctList = listOf(
            binding.itemCharacterMoveClickRightBt,
            binding.itemCharacterMoveClickLeftBt,
            binding.itemCharacterMoveClickMiddleBt
        )
        checkButtonState()
    }

    private val drawableList = listOf(
        R.drawable.selector_ramen_check_1,
        R.drawable.selector_ramen_check_2,
        R.drawable.selector_ramen_check_3
    )


    //버튼 세팅
    private fun checkButtonState() {
        binding.itemCharacterMoveClickLeftBt.setOnClickListener {
            buttonClick(it)
            confirmCorrectQuestion()
        }
        binding.itemCharacterMoveClickMiddleBt.setOnClickListener {
            buttonClick(it)
            confirmCorrectQuestion()
        }
        binding.itemCharacterMoveClickRightBt.setOnClickListener {
            buttonClick(it)
            confirmCorrectQuestion()
        }
    }

    //클릭 함수 모음
    private fun buttonClick(view : View){

        buttonState(view)
        orderNumberCheck()
        onButton()
    }

    // 버튼 활성화 유무
    private fun onButton() {
        val av = requireActivity() as QuizActivity
        if (buttonList.size == 3) {
            av.onButtonState(true)
        } else {
            av.onButtonState(false)
        }
    }

    // 선택, 취소
    private fun buttonState(view: View) {
        if (view.isSelected) {
            view.isSelected = false
            buttonList.remove(view)
        } else {
            view.isSelected = true
            buttonList.add(view)
        }
        Log.d("로그", buttonList.toString())
    }

    //리소스들을 체크해서 백그라운드 지정
    private fun orderNumberCheck() {
        buttonList.mapIndexed { index, v ->
            v.setBackgroundResource(drawableList[index])
        }
    }

    private fun confirmCorrectQuestion(){
          val av = requireActivity() as QuizActivity
          if(buttonList == correctList){
              av.setReplaceLevelState(true)
          }
      }


    override fun onDestroy() {
        super.onDestroy()
        val av = requireActivity() as QuizActivity
        av.onButtonState(false)
        av.setReplaceLevelState(false)
    }
}