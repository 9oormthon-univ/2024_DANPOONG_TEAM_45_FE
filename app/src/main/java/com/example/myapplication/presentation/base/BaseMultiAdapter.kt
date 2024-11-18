package com.example.codingland.presenter.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseMultiAdapter<T, VB : ViewDataBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseMultiAdapter.BaseViewHolder<VB>>(diffCallback) {

    // 아이템 바인딩을 위한 추상 함수
    abstract fun bind(binding: VB, item: T)

    // 각 뷰 타입에 맞는 레이아웃 ID를 반환하는 함수
    abstract fun getLayoutId(viewType: Int): Int

    // 뷰 타입을 결정하는 함수 (추상 함수로 자식 클래스에서 구현)
    abstract fun getViewTypeForItem(item: T): Int

    // ViewType에 따라 다른 레이아웃을 적용
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val layoutId = getLayoutId(viewType) // 뷰 타입에 맞는 레이아웃을 가져옴
        val binding: VB = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), layoutId, parent, false
        )
        return BaseViewHolder(binding)
    }

    // 아이템을 바인딩하는 함수
    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        holder.bind(getItem(position), this)
    }

    // getItemViewType으로 아이템의 뷰 타입을 반환
    override fun getItemViewType(position: Int): Int {
        return getViewTypeForItem(getItem(position))
    }

    // 각 아이템의 ViewHolder를 정의하는 내부 클래스
    class BaseViewHolder<VB : ViewDataBinding>(private val binding: VB) :
        RecyclerView.ViewHolder(binding.root) {
        fun <T> bind(item: T, adapter: BaseMultiAdapter<T, VB>) {
            adapter.bind(binding, item)
            binding.executePendingBindings()
        }
    }
}
