package com.example.myapplication.presentation.adapter

import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCardBinding
import com.example.myapplication.domain.CardItem
import com.example.myapplication.presentation.base.BaseAdapter
import com.example.myapplication.presentation.base.BaseDiffCallback
import com.example.myapplication.presentation.ui.fragment.quest.ItemClickListener

class CardAdapter(private val itemClickListener: ItemClickListener) :
    BaseAdapter<CardItem, ItemCardBinding>(
        BaseDiffCallback(
            itemsTheSame = { oldItem, newItem -> oldItem == newItem },
            contentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )
    ) {

    override val layoutId: Int
        get() = R.layout.item_card


    override fun bind(binding: ItemCardBinding, item: CardItem) {
        with(binding) {
            cardItem = item
            binding.cardIv.setImageResource(item.image)
            binding.root.setOnClickListener {
                itemClickListener.click(item.id)
            }
        }
    }
}