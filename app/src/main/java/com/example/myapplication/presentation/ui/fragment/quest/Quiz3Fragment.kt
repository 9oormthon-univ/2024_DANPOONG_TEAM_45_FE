package com.example.myapplication.presentation.ui.fragment.quest

import android.annotation.SuppressLint
import android.content.ClipData
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCandyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class Quiz3Fragment : BaseFragment<ItemCandyBinding>(R.layout.item_candy) {

    private val answerOrder = listOf(
        R.drawable.activity_qiuz_order_candy_grape_iv,
        R.drawable.activity_qiuz_order_candy_strawberry_iv,
        R.drawable.activity_qiuz_order_candy_mellon_iv,
        R.drawable.activity_qiuz_order_candy_peach_iv,
        R.drawable.activity_qiuz_order_candy_lemon_iv
    )  // 포도-딸기-멜론-복숭아-레몬 순서

    private val droppedOrder = mutableListOf<Int>() // 드롭된 슬롯의 순서에 맞는 이미지 리소스를 저장

    override fun setLayout() {
        initList()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initList() {
        // 각 이미지뷰와 실제 이미지 리소스 ID를 매핑
        val candies = mapOf(
            binding.itemCandyGrapeIv to R.drawable.activity_qiuz_order_candy_grape_iv,
            binding.itemCandyStrawberryIv to R.drawable.activity_qiuz_order_candy_strawberry_iv,
            binding.itemCandyMellonIv to R.drawable.activity_qiuz_order_candy_mellon_iv,
            binding.itemCandyPeachIv to R.drawable.activity_qiuz_order_candy_peach_iv,
            binding.itemCandyLemonIv to R.drawable.activity_qiuz_order_candy_lemon_iv
        )

        val dropTarget = listOf(
            binding.targetSlot1,
            binding.targetSlot2,
            binding.targetSlot3,
            binding.targetSlot4,
            binding.targetSlot5
        )

        // 이미 드롭된 슬롯을 추적하는 변수
        val filledSlots = mutableSetOf<ImageView>()

        // 드래그 시작 설정
        candies.forEach { (imageView, resourceId) ->
            // 올바른 태그 설정
            imageView.tag = resourceId

            imageView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val clipData = ClipData.newPlainText("resourceId", resourceId.toString())
                    val shadowBuilder = View.DragShadowBuilder(v)
                    v.startDragAndDrop(clipData, shadowBuilder, v, 0)
                    true
                } else {
                    false
                }
            }
        }

        // 드롭 타겟 설정
        dropTarget.forEach { target ->
            target.setOnDragListener { view, event ->
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> true

                    DragEvent.ACTION_DRAG_ENTERED -> {
                        view.alpha = 0.5f
                        true
                    }

                    DragEvent.ACTION_DRAG_EXITED -> {
                        view.alpha = 1.0f
                        true
                    }

                    DragEvent.ACTION_DROP -> {
                        view.alpha = 1.0f
                        val draggedView = event.localState as View
                        val draggedImageResId = draggedView.tag as? Int

                        // 드롭된 이미지 리소스가 유효한지 확인
                        if (draggedImageResId != null) {
                            // 이미 캔디가 있는 슬롯이면 아무것도 하지 않음
                            if (target in filledSlots) {
                                Toast.makeText(context, "This slot is already filled!", Toast.LENGTH_SHORT).show()
                                return@setOnDragListener true
                            }

                            // 드롭된 슬롯에 이미지 설정
                            (view as ImageView).setImageResource(draggedImageResId)

                            // 슬롯에 캔디 리소스 ID 저장
                            view.tag = draggedImageResId

                            // 채워진 슬롯 목록에 추가
                            filledSlots.add(target)

                            // 드래그된 아이템 숨김
                            draggedView.visibility = View.INVISIBLE

                            // 현재 순서 업데이트
                            updateDroppedOrder(dropTarget)

                            // 모든 슬롯이 채워졌을 때 정답 확인
                            if (filledSlots.size == answerOrder.size) {
                                checkAnswer()
                            }
                        }

                        true
                    }

                    DragEvent.ACTION_DRAG_ENDED -> {
                        view.alpha = 1.0f
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun updateDroppedOrder(dropTargets: List<ImageView>) {
        // 기존 목록 초기화
        droppedOrder.clear()

        // 각 슬롯에서 이미지 리소스 ID 가져오기
        for (target in dropTargets) {
            val resourceId = target.tag as? Int
            if (resourceId != null) {
                droppedOrder.add(resourceId)
            }
        }
    }

    private fun checkAnswer() {
        if (droppedOrder == answerOrder) {
            Toast.makeText(context, "Correct order!", Toast.LENGTH_LONG).show()
            // TODO: 정답 처리 로직 추가 (다음 화면으로 이동 등)
        } else {
            Toast.makeText(context, "Incorrect order. Try again!", Toast.LENGTH_LONG).show()
            // TODO: 오답 처리 로직 추가
        }
    }
}