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
                if (item.locked) {
                    binding.itemIslandLeftIv.setImageResource(item.island)
                } else {
                    when (item.name) {
                        context.getString(R.string.biginner_island) -> binding.itemIslandLeftIv.setImageResource(R.drawable.iv_biginner_island)
                        else -> binding.itemIslandLeftIv.setImageResource(R.drawable.iv_lake_island_unlocked)
                    }
                }
                binding.root.setBackgroundResource(R.drawable.ic_island_left)
                binding.itemIslandLeftNameTv.text = item.name
                binding.itemIslandLeftIv.setOnClickListener {
                    itemClickListener.click(item)
                }
            }

            is IslandDto.IslandRight -> {
                (binding as ItemIslandRightBinding)
                if (item.locked) {
                    binding.itemIslandRightIv.setImageResource(item.island)
                } else {
                    when (item.name) {
                        context.getString(R.string.candy_island) -> binding.itemIslandRightIv.setImageResource(R.drawable.iv_candy_island_unlocked)
                        else -> binding.itemIslandRightIv.setImageResource(R.drawable.iv_lake_island_locked)
                    }
                }
                binding.root.setBackgroundResource(R.drawable.ic_island_right)
                binding.itemIslandRightNameTv.text = item.name
                binding.itemIslandRightIv.setOnClickListener {
                    itemClickListener.click(item)
                }
            }
        }
    }
}