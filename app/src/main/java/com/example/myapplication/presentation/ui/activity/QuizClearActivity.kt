package com.example.myapplication.presentation.ui.activity

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuizClearBinding
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.viewmodel.CharacterViewModel
import com.example.myapplication.presentation.viewmodel.LoginViewModel
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

    private val loginViewModel: LoginViewModel by viewModels()
    private val characterViewModel: CharacterViewModel by viewModels()
    var cid = ""

    override fun setLayout() {
        initView()
        observeLifeCycle()
    }

    private fun initView() {
        val bool = intent.getBooleanExtra("game2Activity", false)
        lifecycleScope.launch {
            cid = tokenManager.getCharacterId.first().toString()
        }
        if (!bool) {
            binding.tvNextPracticeMoomoo.text = "무무가 멋지게 성장하고 있어요 :)"
            binding.btnNextstageSeeMoomoo.text = "무무 보러가기"
            runBlocking {
                tokenManager.saveTut1("running")
                val score = tokenManager.getCountToken.first().toString().toInt()
                tokenManager.saveCountToken("${score + 1}")
            }
            binding.btnNextstageSeeMoomoo.setOnClickListener {
                //믈약
                characterViewModel.postIncreaseActivity(cid.toInt(), 50)
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
        }
        // 모험 준비하기 클리어
        else {
            binding.tvNextPracticeMoomoo.text = "이제 더 큰 도전을 위한 준비가 되었어요"
            binding.btnNextstageSeeMoomoo.text = "보상받으러 가기"
            lifecycleScope.launch {
                tokenManager.saveTut2("ok")
                val score = tokenManager.getCountToken.first().toString().toInt()
                tokenManager.saveCountToken("${score + 1}")
            }

            loginViewModel.getCompleteTraining()
            binding.btnNextstageSeeMoomoo.setOnClickListener {
                val intent = Intent(this, PotionMysteryActivity::class.java)
                intent.putExtra("potion", 1)
                startActivity(intent)
                finish()
            }
        }
        binding.ivNextStageCancel.setOnClickListener {
            binding.btnNextstageSeeMoomoo.visibility = View.GONE
            finish()
        }
    }

    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                characterViewModel.postIncreaseActivity.collectLatest {
                    when (it.result.code) {
                        200 -> {
                            val intent = Intent(this@QuizClearActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }
}