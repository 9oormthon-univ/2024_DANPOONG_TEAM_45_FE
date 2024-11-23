package com.example.myapplication.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemReclcyerviewQuestBinding
import com.example.myapplication.presentation.ui.fragment.quest.QuestDto
import com.example.myapplication.presentation.widget.extention.loadCropRoundedSquareImage

class QuestChapterAdapter (
    private val context: Context,
    private var questGameList: MutableList<QuestDto>
) : RecyclerView.Adapter<QuestChapterAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(id: Int)
    }

    var itemClickListener: OnItemClickListener?= null

    fun blockVisiblity(visibleBlock: ImageView, goneBlock: ImageView, goneBlock2: ImageView) {
        visibleBlock.visibility = View.VISIBLE
        goneBlock.visibility = View.GONE
        goneBlock2.visibility = View.GONE
    }

    // ViewHolder 클래스
    inner class ViewHolder(private val binding: ItemReclcyerviewQuestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: QuestDto) {
            binding.tvRvGameName.text = item.gameName
            binding.tvRvGameDescript.text = item.gameDescript
//            binding.ibRvGamePic.setImageResource(item.game_img)

            // 게임 진행 상태
            when(item.gameState) {
                0 -> blockVisiblity(binding.ibGameStateIng, binding.ibGameStateIng, binding.ibGameStateDone)
                1 -> blockVisiblity(binding.ibGameStateIng, binding.ibGameStateBefore, binding.ibGameStateDone)
                2 -> blockVisiblity(binding.ibGameStateDone, binding.ibGameStateBefore, binding.ibGameStateIng)
                else -> blockVisiblity(binding.ibGameStateBefore, binding.ibGameStateIng, binding.ibGameStateDone)

            }

            binding.ibRvGamePic.loadCropRoundedSquareImage(item.gameImg, 16)

            binding.ibRvGamePic.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val itemId = questGameList[position].id  // ID 가져오기
                    itemClickListener?.OnItemClick(itemId)  // ID 전달
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReclcyerviewQuestBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("QuestProblemAdapter", "Binding item at position $position")
        holder.bind(questGameList[position])
    }

    override fun getItemCount(): Int {
        return questGameList.size
    }
}
