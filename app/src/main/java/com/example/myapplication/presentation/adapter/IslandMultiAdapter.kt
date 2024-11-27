package com.example.myapplication.presentation.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.example.myapplication.presentation.base.BaseDiffCallback
import com.example.myapplication.presentation.base.BaseMultiAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemIslandLeftBinding
import com.example.myapplication.databinding.ItemIslandRightBinding
import com.example.myapplication.presentation.ui.fragment.quest.IslandDto
import com.example.myapplication.presentation.ui.fragment.quest.ItemClickListener

class IslandMultiAdapter(
    private val context: Context,
    private val itemClickListener : ItemClickListener
) : BaseMultiAdapter<IslandDto, ViewDataBinding>(
    BaseDiffCallback(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    companion object {
        const val VIEW_TYPE_LEFT = 1
        const val VIEW_TYPE_RIGHT = 2
    }

    override fun getLayoutId(viewType: Int) = when (viewType) {
        VIEW_TYPE_LEFT -> R.layout.item_island_left
        VIEW_TYPE_RIGHT -> R.layout.item_island_right
        else -> throw IllegalAccessError("Invalid View Types")
    }

    override fun getViewTypeForItem(item: IslandDto) = when (item) {
        is IslandDto.IslandLeft -> VIEW_TYPE_LEFT
        is IslandDto.IslandRight -> VIEW_TYPE_RIGHT
    }

    override fun bind(binding: ViewDataBinding, item: IslandDto) {

        when (item) {
            is IslandDto.IslandLeft -> {
                (binding as ItemIslandLeftBinding)
                binding.islandData = item
                binding.itemIslandLeftIv.setImageResource(item.island)
                binding.root.setBackgroundResource(item.background)
                binding.itemIslandLeftIv.setOnClickListener {
                    itemClickListener.click(item)
                }
            }

            is IslandDto.IslandRight -> {
                (binding as ItemIslandRightBinding)
                binding.islandData = item
                binding.itemIslandRightIv.setImageResource(item.island)
                binding.root.setBackgroundResource(item.background)
                binding.itemIslandRightIv.setOnClickListener {
                    itemClickListener.click(item)
                }
            }
        }
    }
}