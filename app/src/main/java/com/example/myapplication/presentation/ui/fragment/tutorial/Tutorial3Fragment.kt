package com.example.myapplication.presentation.ui.fragment.tutorial

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentTutorial3Binding
import com.example.myapplication.presentation.ui.activity.BlockDTO
import com.example.myapplication.presentation.ui.activity.QuizBlockActivity
import com.example.myapplication.presentation.ui.activity.TutorialActivity

class Tutorial3Fragment : BaseFragment<FragmentTutorial3Binding>(R.layout.fragment_tutorial3) {

    private var basicBlockId = 1 // 생성되는 블록 아이디 - 블록 색 지정을 위해 만든 변수

    override fun setLayout() {
        initBlock()
    }

    private fun initBlock() {
        val blocks = listOf(
            BlockDTO(resources.getString(R.string.block_type_normal), "준비하기", 0),
            BlockDTO(resources.getString(R.string.block_type_normal), "일어나기", 0),
            BlockDTO(resources.getString(R.string.block_type_normal), "세수하기", 0),
            BlockDTO(resources.getString(R.string.block_type_normal), "아침먹기", 0)
        )
        blocks.forEach { block -> addBlock(block) }
    }

    private fun addBlock(block: BlockDTO) {
        val container = binding.linearLayoutBlockList

        when (block.blockType) {
            resources.getString(R.string.block_type_normal) -> {
                // FrameLayout 생성
                val newBlock = FrameLayout(requireActivity()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    id = basicBlockId
                    basicBlockId += 1
                }

                // ImageView 추가
                val imageView = ImageView(requireActivity()).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER
                        setMargins(0, 0, 0, 5)
                    }

                    // 홀수 번째 / 짝수 번째 블록 색상 지정
                    when (basicBlockId % 2 == 1) {
                        true -> setImageResource(R.drawable.iv_gameblock_basic_type2) // Drawable 리소스 설정
                        else -> setImageResource(R.drawable.iv_gameblock_basic_type1)
                    }
                }

                // TextView 추가
                val textView = TextView(requireActivity()).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.START
                        setMargins(20, 12, 0, 5)
                    }
                    text = block.blockDescript
                    setTextColor(ContextCompat.getColor(context, R.color.white)) // 텍스트 색상
                    textSize = 12f
                }

                // FrameLayout에 ImageView와 TextView 추가
                newBlock.addView(imageView)
                newBlock.addView(textView)

                // `LinearLayout`에 새 `FrameLayout` 추가
                container?.addView(newBlock)
                (activity as? QuizBlockActivity)?.handleViewFromFragment(newBlock)
                newBlock.tag = block  // BlockDTO를 tag로 설정
            }
        }
    }
}