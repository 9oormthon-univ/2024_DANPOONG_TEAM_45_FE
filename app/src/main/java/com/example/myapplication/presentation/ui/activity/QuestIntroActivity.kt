package com.example.myapplication.presentation.ui.activity

import android.content.Intent
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuestIntroBinding
import com.example.myapplication.presentation.widget.extention.loadCropImage
import com.unity3d.player.UnityPlayerGameActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestIntroActivity :
    BaseActivity<ActivityQuestIntroBinding>(R.layout.activity_quest_intro) {


    override fun setLayout() {
        val stage3story = arrayOf("바위가 너무 많잖아!\n" +
                "바위에 걸리지 않고 도착할 수 있게 도와줘ㅠ", "헉 바위가 더 많아졌어ㅠ 바위 위치의 규칙을\n" +
                "알면 쉽게 넘어갈 수 있을 것 같은데?",
            "헉 물이 가득 담긴 양동이들이 길을 막고 있어!\n" +
                    "물이 쏟아지지 않게 양동이를 옮겨줘ㅠ")
        val stage3hint = arrayOf("‘점프하기’ 블록을 사용해 바위를 뛰어넘어요","‘0칸 앞에 바위가 있으면 점프하기’\n" +
                "블럭을 사용해보세요!","양동이가 있을 때는 조건문을 사용해보세요")

        val islandName = intent.getStringExtra("island name")
        val gameId = intent.getIntExtra("game id", -1)

        when (islandName) {
            resources.getString(R.string.biginner_island) -> {
                if (gameId == 1) {
                    binding.tvQuestIntro.text = "코딩 모험의 준비를\n위한 섬에 도착했어요!"
                    binding.ivQuestIntro.loadCropImage(R.drawable.iv_biginner_island)
                    binding.tvQuestIntroSub.text =
                        "먼 길을 떠나기 전, 초심자의 섬 곳곳에 흩어진 \n미션들을 해결하며 기초를 쌓아 볼까요?"
                } else {
                    binding.tvQuestIntro.text = "이제 본격적인\n모험 준비를 해볼까요?"
                    binding.ivQuestIntro.loadCropImage(R.drawable.iv_biginner_island)
                    binding.tvQuestIntroSub.text = "초심자의 섬의 퀘스트를 모두 통과하면\n무무는 더 강해질 수 있어요"
                }
            }

            "사탕의 섬" -> {
                binding.tvQuestIntro.text = "달콤한 향기가\n가득한 섬에 도착했어요!"
                binding.ivQuestIntro.loadCropImage(R.drawable.iv_candy_island_unlocked)
                binding.tvQuestIntroSub.text = "섬 곳곳에 껌과 불 같은 위험한 장애물이\n놓여 있어 조심스럽게 이동해야 해요!"
            }

            "호수의 섬" -> {
                binding.tvQuestIntro.text = "고요하고 반짝이는\n" + "섬에 도착했어요!"
                binding.ivQuestIntro.loadCropImage(R.drawable.iv_candy_island_unlocked)
                binding.tvQuestIntroSub.text = "섬 곳곳에 껌과 불 같은 위험한 장애물이\n놓여 있어 조심스럽게 이동해야 해요!"
            }
        }

        // 툴바를 액티비티에 연결
        val toolbar = binding.tbActivityQuestIntro
        setSupportActionBar(toolbar)

        binding.btnQuestIntroStart.setOnClickListener {
            if (gameId == 1) {
                val intent = Intent(this, QuizActivity::class.java)
                startActivity(intent)
                this.finish()
            } else if (gameId == 2) {
                val intent = Intent(this, TutorialActivity::class.java)
                startActivity(intent)
                this.finish()
            } else if (gameId in 3..7) {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("island name", islandName)
                intent.putExtra("game id", gameId)
                startActivity(intent)
                this.finish()
            } else {
                Intent(this, UnityPlayerGameActivity::class.java).apply {
                    putExtra("hint", stage3hint[gameId %8])
                    putExtra("stage", "${gameId%7}")
                    putExtra("story", stage3story[gameId%8])
                    startActivity(this)
                }
            }

            binding.ivActivityQuestIntroExit.setOnClickListener {
                finish()
            }
        }
    }

}