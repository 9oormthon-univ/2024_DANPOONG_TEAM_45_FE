package com.example.myapplication.presentation.ui.fragment.quest

import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentQuizBlock1Binding
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.BlockDTO
import com.example.myapplication.presentation.ui.activity.GameInterface
import com.example.myapplication.presentation.ui.activity.QuizActivity
import com.example.myapplication.presentation.ui.activity.QuizBlockActivity
import com.example.myapplication.presentation.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizBlock1Fragment : BaseFragment<FragmentQuizBlock1Binding>(R.layout.fragment_quiz_block_1), GameInterface {
    private var targetBlockMap = mutableMapOf<Int, Int?>()
    private lateinit var viewModel: QuizViewModel
    private var moveWay = MutableList(10) { 0 }

    private val dragSources = mutableListOf<View>()
    private var basicBlockId = 1 // 생성되는 블록 아이디 - 블록 색 지정을 위해 만든 변수

    override fun setLayout() {
        //블럭 튜토리얼 1번
        initViewModel()
        initGame()
        initBlock()
        setupDragSources(dragSources)
        updateViewModel()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]
    }

    private fun updateViewModel() {
        viewModel.moveWay.observe(viewLifecycleOwner, Observer { moveWay ->
            // moveWay 값 변경 시 처리할 로직
            this.moveWay = moveWay
            Log.d("moveWay", moveWay.toString())
        })
    }

    override fun initBlock() {
        val blocks = listOf(
            BlockDTO(resources.getString(R.string.block_type_normal), "준비하기", 0),
            BlockDTO(resources.getString(R.string.block_type_normal), "일어나기", 0),
            BlockDTO(resources.getString(R.string.block_type_normal), "세수하기", 0),
            BlockDTO(resources.getString(R.string.block_type_normal), "아침먹기", 0)
        )
        blocks.forEach { block -> addBlock(block) }
    }

    override fun initGame() {
        targetBlockMap = mutableMapOf() // 매팽된 target block map 초기화
        for (dragSource in dragSources) {
            dragSource.visibility = View.VISIBLE
        }
    }

    override fun addBlock(block: BlockDTO) {
        val av = requireActivity() as QuizBlockActivity
        val container = av.binding.linearLayoutBlockList

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

                dragSources.add(newBlock)
                (activity as? QuizBlockActivity)?.handleViewFromFragment(newBlock)
                newBlock.tag = block  // BlockDTO를 tag로 설정
            }
        }
    }

    override fun handleImageDrop(target: View, dragId: Int, dropId: Int) {
        TODO("Not yet implemented")
    }

    override fun checkSuccess() {
        TODO("Not yet implemented")
    }

    override fun showSuccessDialog(exit: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showFailDialog() {
        TODO("Not yet implemented")
    }

    override fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }
}