package com.example.myapplication.presentation.adapter

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemReclcyerviewQuestBinding
import com.example.myapplication.presentation.base.BaseAdapter
import com.example.myapplication.presentation.base.BaseDiffCallback
import com.example.myapplication.presentation.ui.fragment.quest.ItemClickListener
import com.example.myapplication.presentation.ui.fragment.quest.QuestDto
import com.example.myapplication.presentation.widget.extention.loadCropRoundedSquareImage

class QuizAdapter(
    private val listener: ItemClickListener
) : BaseAdapter<QuestDto, ItemReclcyerviewQuestBinding>(
    BaseDiffCallback(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override val layoutId: Int
        get() = R.layout.item_reclcyerview_quest

    private fun blockVisiblity(visibleBlock: ImageView, goneBlock: ImageView, goneBlock2: ImageView) {
        visibleBlock.visibility = View.VISIBLE
        goneBlock.visibility = View.GONE
        goneBlock2.visibility = View.GONE
    }

    fun stateChange(item : QuestDto){
        val list = currentList
        list.map {
            if(it == item){
                it.gameState = 1
            }
        }
        submitList(list)
    }

    override fun bind(binding: ItemReclcyerviewQuestBinding, item: QuestDto) {
        with(binding) {
            itemRecycler = item
            binding.root.setOnClickListener{
                listener.click(item)
            }
            binding.ibRvGamePic.loadCropRoundedSquareImage(item.gameImg, 16)
            if(!item.isOpen){
                binding.itemRecyclerQuestLock.visibility = View.VISIBLE
                binding.ibRvGamePic.foreground = ColorDrawable(ContextCompat.getColor(root.context, R.color.blur_gray))
            }
            when (item.gameState) {
                0 -> blockVisiblity(
                    binding.ibGameStateIng,
                    binding.ibGameStateIng,
                    binding.ibGameStateDone
                )

                1 -> blockVisiblity(
                    binding.ibGameStateIng,
                    binding.ibGameStateBefore,
                    binding.ibGameStateDone
                )

                2 -> blockVisiblity(
                    binding.ibGameStateDone,
                    binding.ibGameStateBefore,
                    binding.ibGameStateIng
                )

                else -> blockVisiblity(
                    binding.ibGameStateBefore,
                    binding.ibGameStateIng,
                    binding.ibGameStateDone
                )

            }
        }
    }
}