package com.example.myapplication.presentation.ui.activity

import android.content.Intent
import android.view.View
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuizClearBinding
import com.example.myapplication.presentation.base.BaseActivity

class QuizClearActivity : BaseActivity<ActivityQuizClearBinding>(R.layout.activity_quiz_clear) {

    override fun setLayout() {

        if (intent.getBooleanExtra("game1Activity", true)) {
            binding.tvNextPracticeMoomoo.text = "무무가 멋지게 성장하고 있어요 :)"
            binding.btnNextstageSeeMoomoo.text = "무무 보러가기"
        }
        // 모험 준비하기 클리어
        else if (intent.getBooleanExtra("game2Activity", true)) {
            binding.tvNextPracticeMoomoo.text = "이제 더 큰 도전을 위한 준비가 되었어요"
            binding.btnNextstageSeeMoomoo.text = "보상받으러 가기"
        }
        binding.ivNextStageCancel.setOnClickListener {
            binding.btnNextstageSeeMoomoo.visibility = View.GONE
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.activity_quiz_clear, QuestChapterFragment())
//                .commit()
            finish()
//            BiginnerFragment().biginner_item[0].game_state = 2 -> TODO game_state 완료로 바꾸기
//            supportFragmentManager.executePendingTransactions()
//            questFragment.updateLakeIslandVisibility(isOn = true)
//
//            val sharedPref = getSharedPreferences("QuestPrefs", Context.MODE_PRIVATE)
//            sharedPref.edit().putBoolean("lakeIslandVisible", true).apply()

        }

        binding.btnNextstageSeeMoomoo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}