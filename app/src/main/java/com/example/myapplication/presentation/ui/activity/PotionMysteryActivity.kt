package com.example.myapplication.presentation.ui.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityPotionMysteryBinding
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.viewmodel.CharacterViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.example.myapplication.presentation.widget.extention.loadCropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PotionMysteryActivity :
    BaseActivity<ActivityPotionMysteryBinding>(R.layout.activity_potion_mystery) {
    @Inject
    lateinit var tokenManager: TokenManager
    private val characterViewModel: CharacterViewModel by viewModels()
    lateinit var cid: String
    var pid = 0
    override fun setLayout() {
        setId()
        setBtnClick()
    }

    private fun setId() {
        pid = intent.getIntExtra("potion", 0)
        initPotion()
    }

    private fun setBtnClick() {
        binding.activityAccountKakaoLoginBt.setOnClickListener {
            characterViewModel.postIncreaseActivity(cid.toInt(), 500)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    private fun initPotion() {
        lifecycleScope.launch {
            cid = tokenManager.getCharacterId.first().toString()
        }

        with(binding.ivPotionMystery) {
            subTitleBinding()
            when (pid) {
                1 -> {
                    loadCropImage(R.drawable.potion1)
                }

                2 -> {
                    loadCropImage(R.drawable.potion2)
                }

                3 -> {
                    loadCropImage(R.drawable.potion3)
                }

                4 -> {
                    loadCropImage(R.drawable.potion4)
                }

                else -> 1
            }
        }
    }

    private fun subTitleBinding() {
        val list = listOf(
            "무무가 전설의 포션을\n" +
                    "먹고 폭풍 성장 했어요!",
            "무무가 신비의 포션을\n" +
                    "먹고 조금 더 성장 했어요!",
            "무무가 스위트 파워 포션을\n" +
                    "먹고 성장 했어요!",
            "푸른 호수의 물결"
        )
        binding.tvPotionMystery.text = list[pid - 1]
    }
}