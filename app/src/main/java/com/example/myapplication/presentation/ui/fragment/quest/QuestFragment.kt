package com.example.myapplication.presentation.ui.fragment.quest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.data.mapper.toDomain
import com.example.myapplication.data.repository.local.dao.ChapterDao
import com.example.myapplication.databinding.FragmentQuestBinding
import com.example.myapplication.presentation.adapter.IslandMultiAdapter
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.viewmodel.ChapterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuestFragment : BaseFragment<FragmentQuestBinding>(R.layout.fragment_quest),
    ItemClickListener {

    @Inject
    lateinit var chapterDao: ChapterDao
    private lateinit var islandAdapter: IslandMultiAdapter
    private val chapterViewModel: ChapterViewModel by viewModels()

    override fun setLayout() {
        initAdapter()
        observeLifeCycle()
    }

    override fun onStart() {
        super.onStart()
        initChapter()
    }

    private fun initChapter() {
        chapterViewModel.getAllChapter()
    }

    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                chapterViewModel.getAllChapter.collectLatest {
                    chapterViewModel.fetchAndSaveChapters(it.payload?.result ?: emptyList())
                    val islandList = it.payload?.toDomain()
                    when (it.result.code) {
                        200 -> {
                            islandAdapter.submitList(islandList)
                            binding.fragmentQuestRv.adapter = islandAdapter
                            Log.d("okhttp", "${islandList}")
                        }
                    }
                }
            }
        }

    }

    private fun initAdapter() {
        islandAdapter = IslandMultiAdapter(requireContext(), this)
    }

    override fun click(item: Any) {
        var name = ""
        var id = 0
        when (item) {
            is IslandDto.IslandLeft -> {
                name = item.name
                id = item.id
            }

            is IslandDto.IslandRight -> {
                name = item.name
                id = item.id
            }
        }

        findNavController().navigate(R.id.action_questFragment_to_questProblemFragment,
            Bundle().apply {
                putString("island name", name)
                putString("selectId", "$id")
            }
        )
    }
}