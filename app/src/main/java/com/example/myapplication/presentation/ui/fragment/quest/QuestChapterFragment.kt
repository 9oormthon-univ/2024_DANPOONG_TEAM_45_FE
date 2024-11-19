package com.example.myapplication.presentation.ui.fragment.quest

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codingland.presenter.base.BaseFragment
import com.example.codingland.presenter.ui.fragment.quest.QuestChapterAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentQuestChapterBinding
import com.example.myapplication.presentation.ui.activity.QuestIntroActivity
import com.example.myapplication.presentation.widget.extention.loadCropImage

class QuestChapterFragment : BaseFragment<FragmentQuestChapterBinding>(R.layout.fragment_quest_chapter) {

    private lateinit var adapter: QuestChapterAdapter
    val questItem = mutableListOf<QuestDto>()

    override fun setLayout() {
        initBiginnerItem()
    }

    private fun initBiginnerItem() {
        val islandName = arguments?.getString("island name")
        if(arguments != null){
            binding.ivBiginnerFlagTxt.text = islandName
            when (islandName) {
                resources.getString(R.string.biginner_island) -> binding.ivBiginnerIslandIcon.loadCropImage(R.drawable.iv_biginner_island)
                resources.getString(R.string.candy_island) -> binding.ivBiginnerIslandIcon.loadCropImage(R.drawable.iv_candy_island_unlocked)
                resources.getString(R.string.lake_island) -> binding.ivBiginnerIslandIcon.loadCropImage(R.drawable.iv_lake_island_unlocked)
                else -> binding.ivBiginnerIslandIcon.loadCropImage(R.drawable.iv_biginner_island)
            }
        }

        addItem()
        adapter = QuestChapterAdapter(requireContext(), questItem)

        binding.rvBiginnerIsland.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = this@QuestChapterFragment.adapter
        }

        // Adapter의 아이템 클릭 리스너 설정
        adapter.itemClickListener = object : QuestChapterAdapter.OnItemClickListener {
            override fun OnItemClick(id: Int) {
                val item = questItem.find { it.id == id }
                item?.let {
                    it.gameState = 1
                    adapter.notifyDataSetChanged() // 어댑터에 변경사항 알림
                }

                when (id) {
                    1 -> {
                        val intent = Intent(requireActivity(), QuestIntroActivity::class.java).apply {
                            putExtra("island name", islandName)
                        }
                        startActivity(intent)
                    }  // ID에 따라 다른 Activity로 전환
                    else -> {
                        // TODO game activity로 이동
                    }
                }
            }
        }
    }

    private fun addItem() {
        val islandName = arguments?.getString("island name")
        when (islandName) {
            resources.getString(R.string.biginner_island) -> {
                with(questItem) {
                    add(QuestDto(1, resources.getString(R.string.game_type_normal), "기초 훈련하기", "초보 모험가를 위한 기초 훈련!", R.drawable.iv_biginner_background_game1,0)) // drawable 리소스 아이디 사용
                    add(QuestDto(2, resources.getString(R.string.game_type_block),"모험 준비하기", "본격적으로 모험을 준비해봐요!", R.drawable.iv_biginner_background_game2,0))
                }
            }
            resources.getString(R.string.candy_island) -> {
                with(questItem) {
                    add(QuestDto(1, resources.getString(R.string.game_type_block),"달콤한 첫 걸음", "사탕의 섬에서의 첫 퀘스트!", R.drawable.iv_candy_background_game1,0)) //
                    add(QuestDto(2, resources.getString(R.string.game_type_block),"사탕을 찾아가자!", "무무가 사탕을 찾도록 도와주세요", R.drawable.iv_candy_background_game2,0))
                    add(QuestDto(3, resources.getString(R.string.game_type_block),"껌을 피하는 법", "무무가 껌을 밟지 않도록 도와주세요", R.drawable.iv_candy_background_game3,0))
                    add(QuestDto(4, resources.getString(R.string.game_type_block),"불 진압하기", "사탕의 섬에 불이 났어요!", R.drawable.iv_candy_background_game4, 0))
                    add(QuestDto(5, resources.getString(R.string.game_type_block),"사탕의 섬을 구해라!", "사탕의 섬이 녹아내리고 있어요", R.drawable.iv_candy_background_game5, 0))
                }
            }
            resources.getString(R.string.lake_island) -> {
                with(questItem) {
                    add(QuestDto(1, resources.getString(R.string.game_type_normal), "기초 훈련하기", "초보 모험가를 위한 기초 훈련!", R.drawable.iv_biginner_background_game1,0))
                    add(QuestDto(2, resources.getString(R.string.game_type_block),"모험 준비하기", "본격적으로 모험을 준비해봐요!", R.drawable.iv_biginner_background_game2,0))
                }
            }
            else -> {
                with(questItem) {
                    add(QuestDto(1, resources.getString(R.string.game_type_normal), "기초 훈련하기", "초보 모험가를 위한 기초 훈련!", R.drawable.iv_biginner_background_game1,0))
                    add(QuestDto(2, resources.getString(R.string.game_type_block),"모험 준비하기", "본격적으로 모험을 준비해봐요!", R.drawable.iv_biginner_background_game2,0))
                }
            }
        }
        println("아이템 수: ${questItem.size}") // 아이템 수 확인용 로그
    }
}
