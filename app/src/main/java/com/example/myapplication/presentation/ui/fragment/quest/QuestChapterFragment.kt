package com.example.myapplication.presentation.ui.fragment.quest

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.data.mapper.toDomain
import com.example.myapplication.databinding.FragmentQuestChapterBinding
import com.example.myapplication.presentation.adapter.QuizAdapter
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.QuestIntroActivity
import com.example.myapplication.presentation.viewmodel.ChapterViewModel
import com.example.myapplication.presentation.viewmodel.LoginViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.example.myapplication.presentation.widget.extention.loadCropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuestChapterFragment :
    BaseFragment<FragmentQuestChapterBinding>(R.layout.fragment_quest_chapter), ItemClickListener {

    private lateinit var adapter: QuizAdapter

    val loginViewModel: LoginViewModel by viewModels()
    private val chapterViewModel: ChapterViewModel by viewModels()

    override fun setLayout() {
        initBiginnerItem()
        observeLifeCycle()
    }

    //이미지 바인딩
    private fun initBiginnerItem() {
        val islandName = arguments?.getString("island name")
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
        addItem()
        adapter = QuizAdapter(this@QuestChapterFragment)
        val id = arguments?.getString("selectId")
        val parseId = id?.toInt() ?: 0
        chapterViewModel.getDistinctChapter(parseId)
    }


    private fun addItem() {
        val islandName = arguments?.getString("island name")
        when (islandName) {
            resources.getString(R.string.biginner_island) -> {

            }
        }
    }


    @Inject
    lateinit var tokenManager: TokenManager
    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                chapterViewModel.getDistinctChapter.collectLatest {
                    when (it.result.code) {
                        200 -> {
                            Log.d("okhttp","${ it.payload?.quizzes}")
                            val list = it.payload?.quizzes?.map { item -> item.toDomain() }
                            binding.rvBiginnerIsland.adapter = adapter
                            adapter.submitList(list)
                            loginViewModel.getTraining()
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                loginViewModel.training.collectLatest {
                    when (it.result.code) {
                        200 -> {

                        }
                    }
                }
            }
        }
    }

    override fun click(item: Any) {
        item as QuestDto
        val intent = Intent(requireActivity(), QuestIntroActivity::class.java).apply {
            val islandName = when(item.id) {
                1, 2 -> getString(R.string.biginner_island)  // "초심자의 섬"
                in 3..7 -> getString(R.string.candy_island)  // "사탕의 섬"
                else -> getString(R.string.lake_island)  // "호수의 섬"
            }
            putExtra("island name", islandName)
            putExtra("game id", item.id )
            Log.d("아이디","${item.id} $islandName")
        }
        startActivity(intent)
    }
}
