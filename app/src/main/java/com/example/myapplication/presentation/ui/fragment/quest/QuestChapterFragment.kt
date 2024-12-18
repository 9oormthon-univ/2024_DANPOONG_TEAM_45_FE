package com.example.myapplication.presentation.ui.fragment.quest

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.data.mapper.toQuestDto
import com.example.myapplication.databinding.FragmentQuestChapterBinding
import com.example.myapplication.presentation.adapter.QuizAdapter
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.PotionMysteryActivity
import com.example.myapplication.presentation.ui.activity.QuestIntroActivity
import com.example.myapplication.presentation.viewmodel.ChapterViewModel
import com.example.myapplication.presentation.viewmodel.LoginViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.example.myapplication.presentation.widget.extention.loadCropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuestChapterFragment :
    BaseFragment<FragmentQuestChapterBinding>(R.layout.fragment_quest_chapter), ItemClickListener {

    private lateinit var adapter: QuizAdapter
    private val loginViewModel: LoginViewModel by viewModels()
    private val chapterViewModel: ChapterViewModel by viewModels()
    private lateinit var customDialog2: CustomDialog2

    override fun setLayout() {
        initBiginnerItem()
        observeLifeCycle()
        onClickBtn()
    }

    //챕터 아이디
    var parseId = 0
    var islandName = ""

    //이미지 바인딩
    private fun initBiginnerItem() {
        islandName = arguments?.getString("island name").toString()
        if (arguments != null) {
            binding.ivBiginnerFlagTxt.text = islandName
            when (islandName) {
                resources.getString(R.string.biginner_island) -> binding.ivBiginnerIslandIcon.loadCropImage(
                    R.drawable.iv_biginner_island
                )

                resources.getString(R.string.candy_island) -> binding.ivBiginnerIslandIcon.loadCropImage(
                    R.drawable.iv_candy_island_unlocked
                )

                resources.getString(R.string.lake_island) -> binding.ivBiginnerIslandIcon.loadCropImage(
                    R.drawable.iv_lake_island_unlocked
                )

                else -> binding.ivBiginnerIslandIcon.loadCropImage(R.drawable.iv_biginner_island)
            }
        }
        adapter = QuizAdapter(this@QuestChapterFragment)
        val id = arguments?.getString("selectId")
        Log.d("okhttp", "$id")
        parseId = id?.toInt() ?: 0
        chapterViewModel.getDistinctChapter(parseId)
        chapterViewModel.fetchQuizzesForChapter(parseId)
    }

    @Inject
    lateinit var tokenManager: TokenManager
    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    chapterViewModel.quizzesForChapter.collectLatest { it ->
                        var count = 0
                        val responseList = it.map { it.toQuestDto() }

                        responseList.forEachIndexed { index, item ->
                            item.isOpen = setIsOpen(responseList.toMutableList(), index)
                        }

                        responseList.forEach { response ->
                            if (response.isCleared) {
                                count++
                            }
                        }

                        binding.rvBiginnerIsland.adapter = adapter
                        adapter.submitList(responseList)

                        binding.ivQuestMoomoo.text = "무무의 퀘스트 (${count}/${responseList?.size})"
                    }
                }

                launch {
                    chapterViewModel.getDistinctChapter.collectLatest {
                        when (it.result.code) {
                            200 -> {
                                if (it.payload?.isRewardButtonActive == true) {
                                    visibleRewardOn()
                                } else {
                                    val clearCount = it.payload?.quizzes?.count { count ->
                                        count.isCleared
                                    }
                                    val questionCount = it.payload?.quizzes?.size
                                    if (clearCount == questionCount) {
                                        visibleRewardOff()
                                    } else {
                                        allOff()
                                    }
                                }
                            }
                            else ->{
                                allOff()
                            }
                        }
                    }
                }
                launch {
                    chapterViewModel.reward.collectLatest {
                        when (it.result.code) {
                            200 -> {
                                visibleRewardOff()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onClickBtn() {
        binding.ibRewardOn.setOnClickListener {
            chapterViewModel.reward(parseId)
            checkTraining()
        }
    }

    private fun allOff() {
        binding.ibRewardOn.visibility = View.GONE
        binding.ibRewardOff.visibility = View.GONE
    }

    private fun visibleRewardOn() {
        binding.ibRewardOn.visibility = View.VISIBLE
        binding.ibRewardOff.visibility = View.GONE
    }

    private fun visibleRewardOff() {
        binding.ibRewardOff.visibility = View.VISIBLE
        binding.ibRewardOn.visibility = View.GONE
    }

    private fun checkTraining() {
        when (parseId) {
            1 -> {
                loginViewModel.getCompleteTraining()
                Intent(requireActivity(), PotionMysteryActivity::class.java).apply {
                    putExtra("potion", 1)
                    startActivity(this)
                }
            }

            2 -> {
                Intent(requireActivity(), PotionMysteryActivity::class.java).apply {
                    putExtra("potion", 3)
                    startActivity(this)
                }
            }
        }
    }

    private fun setIsOpen(list: MutableList<QuestDto>, id: Int): Boolean {
        return when (id) {
            0 -> {
                true
            }

            else -> {
                list[id - 1].isCleared
            }
        }
    }

    private fun setDialog(title: String, subtitle: String) {
        customDialog2 = CustomDialog2(title, subtitle)
        customDialog2.show(requireActivity().supportFragmentManager, "CustomDialog")
    }


    override fun click(item: Any) {
        item as QuestDto
        if (!item.isOpen) {
            setDialog("이전 단계를 클리어 해주세요!", "이 퀘스트는 아직 열리지 않았어요\uD83D\uDE22")
            return
        }
        val intent = Intent(requireActivity(), QuestIntroActivity::class.java).apply {
            val islandName = when (item.id) {
                1, 2 -> getString(R.string.biginner_island)  // "초심자의 섬"
                in 3..7 -> getString(R.string.candy_island)  // "사탕의 섬"
                else -> getString(R.string.lake_island)  // "호수의 섬"
            }
            putExtra("island name", islandName)
            putExtra("game id", item.id)
            Log.d("아이디", "${item.id} $islandName")
        }
        startActivity(intent)
    }
}
