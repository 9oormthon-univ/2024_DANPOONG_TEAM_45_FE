package com.example.myapplication.presentation.ui.activity

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuizClearBinding
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.viewmodel.ChapterViewModel
import com.example.myapplication.presentation.viewmodel.CharacterViewModel
import com.example.myapplication.presentation.viewmodel.LoginViewModel
import com.example.myapplication.presentation.viewmodel.QuizViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class QuizClearActivity : BaseActivity<ActivityQuizClearBinding>(R.layout.activity_quiz_clear) {

    @Inject
    lateinit var tokenManager: TokenManager
    private val isQuizClearedViewModel : QuizViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val characterViewModel: CharacterViewModel by viewModels()
    private val isChapterClearedViewModel: ChapterViewModel by viewModels()
    var cid = ""
    var cStage = ""
    override fun setLayout() {
        initView()
        observeLifeCycle()
    }

    private fun initView() {
        val bool = intent.getBooleanExtra("game2Activity", false)
        cStage = intent.getStringExtra("stage") ?: "";
        lifecycleScope.launch {
            cid = tokenManager.getCharacterId.first().toString()
        }
        if (!bool) {
            binding.tvNextPracticeMoomoo.text = "무무가 멋지게 성장하고 있어요 :)"
            binding.btnNextstageSeeMoomoo.text = "무무 보러가기"
            binding.btnNextstageSeeMoomoo.setOnClickListener {
                //믈약
                increaseExp(300)
            }
        }
        // 모험 준비하기 클리어
        else {
            binding.tvNextPracticeMoomoo.text = "이제 더 큰 도전을 위한 준비가 되었어요"
            binding.btnNextstageSeeMoomoo.text = "보상받으러 가기"

            loginViewModel.getCompleteTraining()
            binding.btnNextstageSeeMoomoo.setOnClickListener {
                if(cStage.isNotEmpty()){
                    handleSuccess()
                }
                increaseExp(50)
            }
        }
        binding.ivNextStageCancel.setOnClickListener {
            binding.btnNextstageSeeMoomoo.visibility = View.GONE
            finish()
        }
    }

    private fun handleSuccess() {
        val curStage = cStage.toInt()
        Log.d("스테이지",curStage.toString())
        isQuizClearedViewModel.postQuizClear(curStage)
        if (curStage == 10) { // 마지막 퀴즈이면 챕터 클리어 POST
            Log.d("okhttp", "클리어")
            isChapterClearedViewModel.postChapterClear(3)
        }
    }


    private fun increaseExp(exp : Int){
        characterViewModel.postIncreaseActivity(cid.toInt(), exp)
    }

    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                characterViewModel.postIncreaseActivity.collectLatest {
                    when (it.result.code) {
                        200 -> {
                            val intent = Intent(this@QuizClearActivity, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}