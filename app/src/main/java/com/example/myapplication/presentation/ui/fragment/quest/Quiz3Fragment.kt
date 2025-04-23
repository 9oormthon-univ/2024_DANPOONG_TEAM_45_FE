package com.example.myapplication.presentation.ui.fragment.quest

import android.annotation.SuppressLint
import android.content.ClipData
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemAlgorithmBinding
import com.example.myapplication.databinding.ItemCandyBinding
import com.example.myapplication.presentation.ui.activity.QuizActivity
import com.example.myapplication.presentation.widget.extention.loadCropRoundedSquareImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class Quiz3Fragment :
    BaseFragment<ItemCandyBinding>(R.layout.item_candy) {

    private var selectedNumber = 0

    private lateinit var viewList: List<View>

    override fun setLayout() {
        initList()
        //btnClick()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initList() {
        val candies = listOf(
            binding.itemCandyGrapeIv,
            binding.itemCandyLemonIv,
            binding.itemCandyPeachIv,
            binding.itemCandyStrawberryIv,
            binding.itemCandyMellonIv
        )

        val dropTargets = listOf(
            binding.targetSlot1,
            binding.targetSlot2,
            binding.targetSlot3,
            binding.targetSlot4,
            binding.targetSlot5
        )

        // 드래그 시작 설정
        candies.forEach { candy ->
            candy.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val clipData = ClipData.newPlainText("label", "")
                    val shadowBuilder = View.DragShadowBuilder(v)
                    v.startDragAndDrop(clipData, shadowBuilder, v, 0)
                    true
                } else {
                    false
                }
            }
        }

        dropTargets.forEach { target ->
            target.setOnDragListener { view, event ->
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> true

                    DragEvent.ACTION_DRAG_ENTERED -> {
                        view.alpha = 0.5f // 드래그가 들어오면 alpha 감소
                        true
                    }

                    DragEvent.ACTION_DRAG_EXITED -> {
                        view.alpha = 1.0f // 드래그가 나가면 alpha 복구
                        true
                    }

                    DragEvent.ACTION_DROP -> {
                        view.alpha = 1.0f // 드롭 후 alpha 복구

                        // 드래그된 뷰 가져오기
                        val draggedView = event.localState as View

                        // 드롭된 슬롯에 이미지를 추가하는 코드
                        if (draggedView is ImageView) {
                            val targetSlot = view as ImageView
                            // 기존 이미지를 slot에 표시
                            targetSlot.setImageDrawable(draggedView.drawable)

                            // 원본 이미지를 제거하거나 복제하려면 필요에 따라 처리할 수 있음
                            draggedView.visibility = View.INVISIBLE // 원본 이미지는 숨김

                            // 원하는 동작 예시
                            Toast.makeText(requireContext(), "Candy dropped!", Toast.LENGTH_SHORT).show()
                        }

                        true
                    }

                    DragEvent.ACTION_DRAG_ENDED -> {
                        view.alpha = 1.0f // 드래그 종료 후 alpha 복구
                        true
                    }

                    else -> false
                }
            }
        }
    }
}