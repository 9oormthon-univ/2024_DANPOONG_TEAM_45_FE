package com.example.myapplication.presentation.ui.activity

import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityHeroCactusBinding
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.widget.extention.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class HeroCactusActivity : BaseActivity<ActivityHeroCactusBinding>(R.layout.activity_hero_cactus) {

    @Inject
    lateinit var tokenManager: TokenManager
    override fun setLayout() {
        initView()
        setOnClick()
        showToast()
    }

    private fun initView() {
        val name = intent.getStringExtra("cactusName")
        val star = intent.getStringExtra("star")
        binding.tvPotionMystery.text = "$name 등장"
        binding.tvPotionMysterySub.text = "이제부터 ${name}을 키워요"
        Log.d("okhttp", "$name $star")
        val img = when (name) {
            "마법사 선인장" -> R.drawable.ic_cactus_magic
            "영웅 선인장" -> R.drawable.ic_cactus_hero
            else -> 0
        }
        binding.ivCharacter.setImageResource(img)
        binding.start2.text = "레어도 $star"
    }


    private fun setOnClick() {
        binding.ivActivityPotionMysteryCancel.setOnClickListener {
            finish()
        }
    }

    private fun showToast() {
        val toast = Toast.makeText(this, "새로운 선인장이 등록되었어요! 도감을 확인해 주세요!", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }
}