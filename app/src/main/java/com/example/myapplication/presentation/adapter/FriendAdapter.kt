package com.example.myapplication.presentation.adapter

import com.example.myapplication.R
import com.example.myapplication.databinding.ItemFriendBinding
import com.example.myapplication.domain.model.FriendsEntity
import com.example.myapplication.presentation.base.BaseAdapter
import com.example.myapplication.presentation.base.BaseDiffCallback
import com.example.myapplication.presentation.widget.extention.loadProfileImage

class FriendAdapter : BaseAdapter<FriendsEntity, ItemFriendBinding>(
    BaseDiffCallback(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override val layoutId: Int
        get() = R.layout.item_friend

    override fun bind(binding: ItemFriendBinding, item: FriendsEntity) {
        with(binding) {
            friendData = item
            itemFriendIv.loadProfileImage(item.profile)
            binding.itemFriendRankTv.text = item.rank
        }
    }
}