package com.example.myapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemBannerListItemBinding

class LoginBannerViewPagerAdapter(private val imageList: ArrayList<Int>) :
    RecyclerView.Adapter<LoginBannerViewPagerAdapter.PagerViewHolder>() {

    inner class PagerViewHolder(
        private val binding: ItemBannerListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageRes: Int) {
            binding.itemBannerIv.setImageResource(imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = ItemBannerListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val realPosition = position % imageList.size
        holder.bind(imageList[realPosition])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}