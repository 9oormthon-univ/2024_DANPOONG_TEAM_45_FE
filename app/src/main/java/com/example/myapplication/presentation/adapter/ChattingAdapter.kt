package com.example.myapplication.presentation.adapter

import androidx.databinding.ViewDataBinding
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemLeftBinding
import com.example.myapplication.databinding.ItemRightBinding
import com.example.myapplication.domain.model.ChatMessage
import com.example.myapplication.presentation.base.BaseDiffCallback
import com.example.myapplication.presentation.base.BaseMultiAdapter

class ChattingAdapter(
    private val itemClickedListener: AdapterItemClickedListener
) : BaseMultiAdapter<ChatMessage, ViewDataBinding>(
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
        VIEW_TYPE_LEFT -> R.layout.item_left
        VIEW_TYPE_RIGHT -> R.layout.item_right
        else -> throw IllegalAccessError("Invalid View Types")
    }

    override fun getViewTypeForItem(item: ChatMessage) = when (item) {
        is ChatMessage.LEFT -> VIEW_TYPE_LEFT
        is ChatMessage.RIGHT -> VIEW_TYPE_RIGHT
    }

    override fun bind(binding: ViewDataBinding, item: ChatMessage) {
        when (item) {
            is ChatMessage.LEFT -> (binding as ItemLeftBinding).aiItem = item
            is ChatMessage.RIGHT -> (binding as ItemRightBinding).aiItem = item
        }
    }

}