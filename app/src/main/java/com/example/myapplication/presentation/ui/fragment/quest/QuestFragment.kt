package com.example.codingland.presenter.ui.fragment.quest

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.codingland.presenter.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentQuestBinding
import com.example.myapplication.presentation.ui.fragment.quest.IslandDto
import com.example.myapplication.presentation.adapter.IslandMultiAdapter
import com.example.myapplication.presentation.ui.fragment.quest.ItemClickListener

class QuestFragment : BaseFragment<FragmentQuestBinding>(R.layout.fragment_quest),
    ItemClickListener {
    private lateinit var islandAdapter: IslandMultiAdapter

    override fun setLayout() {
        initAdapter()
    }

    private fun initAdapter() {
        val islandList = listOf(
            IslandDto.IslandLeft(
                R.drawable.ic_island_left,
                R.drawable.iv_biginner_island,
                false,
                resources.getString(R.string.biginner_island)
            ),
            IslandDto.IslandRight(
                R.drawable.ic_island_right,
                R.drawable.iv_candy_island_locked,
                true,
                resources.getString(R.string.candy_island)
            ),
            IslandDto.IslandLeft(
                R.drawable.ic_island_left,
                R.drawable.iv_lake_island_locked,
                true,
                resources.getString(R.string.lake_island)
            ),
            IslandDto.IslandRight(
                R.drawable.ic_island_right,
                R.drawable.iv_lake_island_locked,
                true,
                "햇살의 섬"
            )
        )
        islandAdapter = IslandMultiAdapter(requireContext(), this)
        binding.fragmentQuestRv.adapter = islandAdapter
        islandAdapter.submitList(islandList)
    }

    override fun click(item: Any) {
        var name = ""
        when (item) {
            is IslandDto.IslandLeft -> name = item.name
            is IslandDto.IslandRight -> name = item.name
        }

        findNavController().navigate(R.id.action_questFragment_to_questProblemFragment,
            Bundle().apply {
                putString("island name", name)
            }
        )
    }
}