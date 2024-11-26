package com.example.myapplication.presentation.ui.fragment.quest

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.DRAG_FLAG_GLOBAL
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.DragStartHelper
import androidx.draganddrop.DropHelper
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentQuizBlock1Binding
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.BlockDTO
import com.example.myapplication.presentation.ui.activity.GameInterface
import com.example.myapplication.presentation.ui.activity.QuizBlockActivity

class QuizBlock1Fragment : BaseFragment<FragmentQuizBlock1Binding>(R.layout.fragment_quiz_block_1), GameInterface {

    private val dropTargets by lazy {
        mutableListOf<View>(
            requireActivity().findViewById(R.id.ib_biginner_game1_space1),
            requireActivity().findViewById(R.id.ib_biginner_game1_space2),
            requireActivity().findViewById(R.id.ib_biginner_game1_space3),
            requireActivity().findViewById(R.id.ib_biginner_game1_space4)
        )
    }

    private var targetBlockMap = mutableMapOf<Int, Int?>()
    private var isExit = false //나가기 버튼 클릭했는지 여부 판단
    private var isDialogShown = false // 다이얼로그 표시 상태 플래그
    private var isFailDialogShown = false

    private val dragSources = mutableListOf<View>()
    private var basicBlockId = 1 // 생성되는 블록 아이디 - 블록 색 지정을 위해 만든 변수
    private var repeatBlockId = 1 // 생성되는 블록 아이디
    private var curGameId = 2
    private var chapterId = 1

    private var isState = false

    private var moveXCnt = 0
    private var moveYCnt = 0
    private var moveWay = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    private var isRepeat = false
    private var repeatIdx: Int = -1
    private var draggedTextView: TextView? = null

    override fun setLayout() {
        //블럭 튜토리얼 1번
        initBlock()
        setupDragSources(dragSources)
        setupDropTargets(dropTargets, requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뷰가 완전히 초기화된 후에 작업
    }


    override fun initViewModel() {
        TODO("Not yet implemented")
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
        isExit = false
        isDialogShown = false

        targetBlockMap = mutableMapOf()
        for (dragSource in dragSources) {
            dragSource.visibility = View.VISIBLE
        }
        isFailDialogShown = false
        blockVisibility(requireActivity().findViewById<ImageButton>(R.id.ib_gameplay_btn), requireActivity().findViewById<ImageButton>(R.id.ib_gamestop_btn))
    }

    override fun addBlock(block: BlockDTO) {
        val container = requireActivity().findViewById<LinearLayout>(R.id.linearLayout_block_list)

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
                newBlock.tag = block  // BlockDTO를 tag로 설정
            }
        }
    }

    // drop의 target id 찾기
    override fun setupDropTargets(dropTargets: List<View>, context: Context) {
        val activity = QuizBlockActivity()

        dropTargets.forEach { target ->
            DropHelper.configureView(
                activity,
                target,
                arrayOf(MIMETYPE_TEXT_PLAIN),
                DropHelper.Options.Builder()
                    .setHighlightColor(getColor(context, R.color.water_color))
                    .build()
            ) { view, payload ->
                val item = payload.clip.getItemAt(0)
                val imageResId = item.text.toString().toIntOrNull()
                if (imageResId != null) {
                    val dropTargetId = dropTargets.indexOf(target)
                    handleImageDrop(view, imageResId, dropTargetId)
                } else {
                    Log.e(TAG, "Failed to retrieve imageResId from ClipData")
                }

                // 드롭 후 다른 데이터 처리
                payload.partition { it == item }.second
            }
        }
    }

    override fun handleImageDrop(target: View, dragId: Int, dropId: Int) {
        // target을 Fragment 내에서 적절히 참조
        targetBlockMap[dropId] = dragId
        dragSources[dragId].visibility = View.GONE

        val draggedBlock = dragSources[dragId] as FrameLayout
        val blockDTO = draggedBlock.tag as? BlockDTO
        val blockMove = blockDTO?.blockDescript
        val targetView = target as? FrameLayout

        if (targetView != null) {
            targetView.visibility = View.VISIBLE // target이 보이도록 설정

            // 기존 드래그된 이미지와 텍스트를 업데이트
            val targetImageView = targetView.getChildAt(0) as ImageView
            val draggedImageView = draggedBlock.getChildAt(0) as ImageView
            targetImageView.setImageDrawable(draggedImageView.drawable)

            // 텍스트 뷰 업데이트
            if (targetView.childCount > 1) {
                targetView.removeViewAt(1)
            }

            // 새 텍스트 뷰 추가
            val overlayTextView = TextView(requireContext()).apply {
                text = draggedTextView?.text
                textSize = 12f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                setPadding(20, 25, 0, 0)
            }
            targetView.addView(overlayTextView, 1)  // 새 텍스트 뷰를 두 번째 위치에 추가
        } else {
            Log.e("handleImageDrop", "Target is not a FrameLayout")
        }

        // 블록 이동 처리
        handleBlockMove(blockMove!!, dropId)
    }


    override fun checkSuccess() {
        if (isDialogShown) return

        var correctBlockOrder = listOf(0)
        var successCnt = 0
        correctBlockOrder = listOf(R.string.game_wake, R.string.game_wash, R.string.game_breakfast, R.string.game_practice)

        for (i: Int in correctBlockOrder.indices) {
            if (moveWay[i] == correctBlockOrder[i]) {
                successCnt += 1
            }
        }
        var success : Boolean
        if (successCnt == correctBlockOrder.size) success = true
        else success = false
        //********
        if (success) {
            isDialogShown = true
            Log.d("cur id testtest", curGameId.toString())
//            isQuizClearedViewModel.postQuizClear(curGameId)
            // 성공 다이얼로그 출력
            showSuccessDialog(false)
        } else {
            // 실패 다이얼로그 출력
            if (!isFailDialogShown) { // 실패 다이얼로그가 이미 표시되지 않았으면
                showFailDialog()
                isFailDialogShown = true
            }
        }
    }

    private fun handleBlockMove(blockMove: String, dropId: Int) {
        val blockMoveMap = mapOf(
            resources.getString(R.string.game_wake) to R.string.game_wake,
            resources.getString(R.string.game_wash) to R.string.game_wash,
            resources.getString(R.string.game_practice) to R.string.game_practice,
            resources.getString(R.string.game_breakfast) to R.string.game_breakfast,
        )

        val move = blockMoveMap[blockMove]
        if (move != null) {
            moveWay[dropId] = move
        } else {
            moveWay[dropId] = -1
        }
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

//    private fun buttonSet(isState: Boolean) {
//        val av = requireActivity() as QuizBlockActivity
//        av.onStoryState(isState)
//        av.onGameplayState(isState)
//    }
//
//    private fun buttonState(view1: View, view2: View) {
//        view1.isSelected = true
//        view2.isSelected = false
//    }


}