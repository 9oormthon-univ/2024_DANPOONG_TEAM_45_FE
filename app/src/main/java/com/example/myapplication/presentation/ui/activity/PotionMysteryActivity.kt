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
    private val characterViewModel : CharacterViewModel by viewModels()
    lateinit var cid : String
    override fun setLayout() {
        initPotion()
        setBtnClick()
    }

    private fun setBtnClick() {
        binding.activityAccountKakaoLoginBt.setOnClickListener {
            characterViewModel.postIncreaseActivity(cid.toInt(),200)
            startActivity(Intent(this@PotionMysteryActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun initPotion() {
        lifecycleScope.launch {
            cid = tokenManager.getCharacterId.first().toString()
        }

        val id = intent.getIntExtra("potion", 0)
        with(binding.ivPotionMystery) {
            when (id) {
                1 -> loadCropImage(R.drawable.potion1)
                2 -> loadCropImage(R.drawable.potion2)
                3 -> loadCropImage(R.drawable.potion3)
                4 -> loadCropImage(R.drawable.potion4)
                else -> 1
            }
        }
    }
}