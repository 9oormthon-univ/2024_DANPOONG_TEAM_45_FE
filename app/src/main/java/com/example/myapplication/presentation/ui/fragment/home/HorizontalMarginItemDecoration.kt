package com.example.myapplication.presentation.ui.fragment.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalMarginItemDecoration(
    private val firstItemMargin: Int,
    private val lastItemMargin: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            outRect.left = firstItemMargin
        }

        if (position == state.itemCount - 1) {
            outRect.right = lastItemMargin
        }
    }
}
