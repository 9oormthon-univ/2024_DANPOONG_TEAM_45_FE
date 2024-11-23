package com.example.myapplication.presentation.ui.activity

import android.content.Intent
import android.util.Log
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuestIntroBinding
import com.example.myapplication.presentation.widget.extention.loadCropImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestIntroActivity :
    BaseActivity<ActivityQuestIntroBinding>(R.layout.activity_quest_intro) {


    override fun setLayout() {

        val islandName = intent.getStringExtra("island name")
        val gameId = intent.getIntExtra("game id", -1)
        Log.d("아이디 ㄴ","${gameId} ${islandName}")

        when (islandName) {
            resources.getString(R.string.biginner_island) -> {
                if (gameId == 1){
                    binding.tvQuestIntro.text = "코딩 모험의 준비를\n위한 섬에 도착했어요!"
                    binding.ivQuestIntro.loadCropImage(R.drawable.iv_biginner_island)
                    binding.tvQuestIntroSub.text = "먼 길을 떠나기 전, 초심자의 섬 곳곳에 흩어진 \n미션들을 해결하며 기초를 쌓아 볼까요?"
                }
                else {
                    binding.tvQuestIntro.text = "이제 본격적인\n모험 준비를 해볼까요?"
                    binding.ivQuestIntro.loadCropImage(R.drawable.iv_biginner_island)
                    binding.tvQuestIntroSub.text = "초심자의 섬의 퀘스트를 모두 통과하면\n무무는 더 강해질 수 있어요"
                }
            }
            else -> {
                binding.tvQuestIntro.text = "달콤한 향기가\n가득한 섬에 도착했어요!"
                binding.ivQuestIntro.loadCropImage(R.drawable.iv_candy_island_unlocked)
                binding.tvQuestIntroSub.text = "섬 곳곳에 껌과 불 같은 위험한 장애물이\n놓여 있어 조심스럽게 이동해야 해요!"
            }
        }

        // 툴바를 액티비티에 연결
        val toolbar = binding.tbActivityQuestIntro
        setSupportActionBar(toolbar)


        binding.btnQuestIntroStart.setOnClickListener {
            if (islandName == resources.getString(R.string.biginner_island) && gameId == 1) {
                val intent = Intent(this, QuizActivity::class.java)
                startActivity(intent)
                this.finish()
            }
            else {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("island name", islandName)
                intent.putExtra("game id", gameId)
                startActivity(intent)
                this.finish()
            }
        }
        binding.ivActivityQuestIntroExit.setOnClickListener {
            finish()
        }
    }


}