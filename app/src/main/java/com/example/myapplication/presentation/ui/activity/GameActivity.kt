package com.example.myapplication.presentation.ui.activity

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ContentValues.TAG
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.DRAG_FLAG_GLOBAL
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.DragStartHelper
import androidx.draganddrop.DropHelper
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityGameBinding
import com.example.myapplication.presentation.base.BaseActivity

class GameActivity : BaseActivity<ActivityGameBinding>(R.layout.activity_game) {

    private var isFailDialogShown = false
    private var targetBlockMap = mutableMapOf<Int, Int?>()

    private val dragSources = mutableListOf<View>()
    private var basicBlockId = 1 // 생성되는 블록 아이디
    private var repeatBlockId = 1 // 생성되는 블록 아이디

    private val dropTargets by lazy {
        listOf(
            binding.ibBiginnerGame1Space1,
            binding.ibBiginnerGame1Space2,
            binding.ibBiginnerGame1Space3,
            binding.ibBiginnerGame1Space4
        )
    }

    override fun setLayout() {
        addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "준비하기", 0))
        addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "일어나기", 0))
        addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "세수하기", 0))
        addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "아침먹기", 0))
        addBlock(BlockDTO(resources.getString(R.string.block_type_repeat), "번 반복하기", 3))
        initGame()
        gameFunction()
        setupDragSources()
        setupDropTargets()
    }

    private fun setupDragSources() {
        dragSources.forEach { source ->
            DragStartHelper(source) { view, _ ->
                var imageResId = dragSources.indexOf(source)
                Log.d("image resource id", imageResId.toString())
                val dragClipData = ClipData.newPlainText("DragData", imageResId.toString())
                dragClipData.addItem(ClipData.Item(imageResId.toString()))

                // Set the visual appearance of the drag shadow
                val dragShadow = View.DragShadowBuilder(view)

                // Start the drag and drop process
                view.startDragAndDrop(
                    dragClipData,
                    dragShadow,
                    null,
                    DRAG_FLAG_GLOBAL
                )
                true
            }.attach()
        }
    }

    private fun setupDropTargets() {
        dropTargets.forEach { target ->
            DropHelper.configureView(
                this,
                target,
                arrayOf(MIMETYPE_TEXT_PLAIN),
                DropHelper.Options.Builder()
                    .setHighlightColor(getColor(R.color.water_color))
                    .build()
            ) { view, payload ->
                val item = payload.clip.getItemAt(0)
                val imageResId = item.text.toString().toIntOrNull()
                if (imageResId != null) {
                    var dropTargetId = dropTargets.indexOf(target)

                    if (targetBlockMap.contains(dropTargetId) && targetBlockMap[dropTargetId] != imageResId) {
                        // 블록 중복해서 놓을 경우 : 블록을 다시 원래 자리로 갖다 놓기
                        dragSources[targetBlockMap[dropTargetId]!!].visibility = View.VISIBLE
                    }
                    Log.d("test drop target id", dropTargets.indexOf(target).toString())
                    handleImageDrop(view, imageResId, dropTargetId)
                } else {
                    Log.e(TAG, "Failed to retrieve imageResId from ClipData")
                }

                // 드롭 후 다른 데이터 처리
                payload.partition { it == item }.second
            }
        }
    }

    private fun handleImageDrop(target: View, dragId: Int, dropId: Int) {
        targetBlockMap[dropId] = dragId
        dragSources[dragId].visibility = View.GONE

        // 드래그된 View (FrameLayout)에서 ImageView와 TextView를 가져옴
        val draggedBlock = dragSources[dragId] as FrameLayout
        val BlockDTO = draggedBlock.tag as? BlockDTO
        val blockType = BlockDTO?.blockType

        when (blockType) {
            resources.getString(R.string.block_type_normal) -> {
                // 드래그된 View (FrameLayout)에서 ImageView와 TextView를 가져옴
                val draggedImageView = draggedBlock.getChildAt(0) as ImageView
                val draggedTextView = draggedBlock.getChildAt(1) as TextView

                if (target is FrameLayout) {
                    // 기존 ImageView를 target에 덮어씌우기
                    val targetImageView = target.getChildAt(0) as ImageView
                    targetImageView.setImageDrawable(draggedImageView.drawable)

                    // 기존 TextView가 있다면 제거
                    if (target.childCount > 1) {
                        target.removeViewAt(1)
                    }

                    // TextView를 target에 새로 추가
                    val overlayTextView = TextView(this).apply {
                        text = draggedTextView.text
                        textSize = 12f
                        setTextColor(ContextCompat.getColor(context, R.color.white)) // 텍스트 색상
                        setPadding(20, 25, 0, 0)
                    }

                    target.addView(overlayTextView, 1)
                }
            }

            resources.getString(R.string.block_type_repeat) -> {
                target.layoutParams = target.layoutParams.apply {
                    height = dragSources[dragId].height
                    width = dragSources[dragId].width
                }
                for (dropTarget in dropTargets) {
                    if (dropTarget != dropTargets[dropId] && dragId == 0) {
                        dropTarget.visibility = View.VISIBLE
                    }
                }

                // REPEAT 블록 처리 (드래그된 블록의 ImageView 및 EditText 처리)
                val draggedImageView1 = draggedBlock.getChildAt(0) as ImageView
//                val draggedImageView2 = draggedBlock.getChildAt(1) as ImageView
//                val draggedImageView3 = draggedBlock.getChildAt(2) as ImageView
//                val draggedEditText = draggedBlock.getChildAt(3) as EditText

                if (target is FrameLayout) {
                    // 기존 ImageView들을 target에 덮어씌우기
                    val targetImageView1 = target.getChildAt(0) as ImageView
                    targetImageView1.setImageDrawable(draggedImageView1.drawable)
//                    val targetImageView2 = target.getChildAt(0) as ImageView
//                    targetImageView1.setImageDrawable(draggedImageView2.drawable)
//                    val targetImageView3 = target.getChildAt(0) as ImageView
//                    targetImageView3.setImageDrawable(draggedImageView3.drawable)

                    // 기존 EditText가 있다면 제거
                    if (target.childCount > 2) {
                        target.removeViewAt(2)
                    }

//                    // EditText를 target에 새로 추가
//                    val overlayEditText = EditText(this).apply {
//                        setText(draggedEditText.text)
//                        setTextColor(ContextCompat.getColor(this@GameActivity, R.color.white))
//                        textSize = 10.51f
//                        setPadding(20, 25, 0, 0)
//                    }
//
//                    target.addView(overlayEditText, 3)
                }
            }

            else -> {
                // 블록 타입이 정의되지 않았을 경우 처리
            }
        }
    }

    private fun checkSuccess() {
        // 올바른 블록이 각 dropTarget에 들어왔는지 확인
        val correctBlockOrder = listOf(1, 2, 3, 0)
        var success = true

        // 각 dropTarget에 대한 검증
        dropTargets.forEachIndexed { index, target ->
            val droppedBlock = targetBlockMap[index]
            if (droppedBlock == null) {
                Log.d(TAG, "타겟 ${index + 1}에 블록이 드롭되지 않았습니다.")
                success = false
            } else if (droppedBlock != correctBlockOrder[index]) {
                Log.d(
                    TAG,
                    "타겟 ${index + 1}에 잘못된 블록이 드롭되었습니다. 예상: ${correctBlockOrder[index]}, 실제: $droppedBlock"
                )
                success = false
            }
        }

        // 결과 출력
        if (success) {
            // 성공 다이얼로그 출력
            showSuccessDialog()
        } else {
            // 실패 다이얼로그 출력
            if (!isFailDialogShown) { // 실패 다이얼로그가 이미 표시되지 않았으면
                showFailDialog()
                isFailDialogShown = true
            }
        }
    }

    private fun gameFunction() {
        // 각종 버튼들 처리
        binding.ibGameplayBtn.setOnClickListener {
            blockVisibility(binding.ibGamestopBtn, binding.ibGameplayBtn)
            checkSuccess()
        }
        binding.ibGamestopBtn.setOnClickListener {
            blockVisibility(binding.ibGameplayBtn, binding.ibGamestopBtn)
        }
        binding.ibGameplayExitBtn.setOnClickListener {
            finish()
        }

        binding.ibBulbBtn.setOnClickListener {
            binding.ibBulbBtn.isSelected = !binding.ibBulbBtn.isSelected
            if (binding.ibBulbBtn.isSelected) {
                // 힌트 보여주기
                binding.ivGameHint.visibility = View.VISIBLE
            } else {
                binding.ivGameHint.visibility = View.GONE
            }
        }

        binding.ibGamestoryOn.setOnClickListener {
            binding.ibGamestoryMsg.visibility = View.GONE
            binding.ibGamestoryMsgTxt.visibility = View.GONE
            blockVisibility(binding.ibGamestoryOff, binding.ibGamestoryOn)
        }
        binding.ibGamestoryOff.setOnClickListener {
            binding.ibGamestoryMsg.visibility = View.VISIBLE
            binding.ibGamestoryMsgTxt.visibility = View.VISIBLE
            blockVisibility(binding.ibGamestoryOn, binding.ibGamestoryOff)
        }
    }

    private fun blockVisibility(visibleBlock: ImageButton, goneBlock: ImageButton) {
        visibleBlock.visibility = View.VISIBLE
        goneBlock.visibility = View.GONE
    }

    private fun showSuccessDialog() {
        // 다이얼로그 레이아웃을 불러옴
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_success, null)

        // 커스텀 다이얼로그 생성
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)  // 다이얼로그 외부 터치로 종료되지 않도록

        // 다이얼로그 만들기
        val dialog = dialogBuilder.create()

        val title = dialogView.findViewById<TextView>(R.id.dialog_button_two_title)
        val subTitle = dialogView.findViewById<TextView>(R.id.dialog_button_two_subtitle)
        val stopBtn = dialogView.findViewById<Button>(R.id.dialog_button_stop)
        val nextBtn = dialogView.findViewById<Button>(R.id.dialog_button_next_step)

        title.text = "완벽한 아침이야!"
        subTitle.text = "상쾌한 아침을 만들어줘서 고마워 :)"

        stopBtn.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        nextBtn.setOnClickListener {
//            val intent = Intent(this, Game2Activity::class.java)
//            startActivity(intent)
            finish()
        }
        // 다이얼로그 보여주기
        dialog.show()
    }

    private fun showFailDialog() {
        // 다이얼로그 레이아웃을 불러옴
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_fail, null)

        // 커스텀 다이얼로그 생성
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)  // 다이얼로그 외부 터치로 종료되지 않도록

        // 다이얼로그 만들기
        val dialog = dialogBuilder.create()

        val retryBtn = dialogView.findViewById<Button>(R.id.btn_dialog_biginner_quiz_fail)

        retryBtn.setOnClickListener {
            initGame()
            dialog.dismiss()  // 다이얼로그 닫기
        }
        // 다이얼로그 보여주기
        dialog.show()
    }

    private fun initGame() {
        targetBlockMap = mutableMapOf()
        for (dragSource in dragSources) {
            dragSource.visibility = View.VISIBLE
        }

        dropTargets.forEach { target ->
            // 기존 ImageView 리셋
            val targetImageView = target.getChildAt(0) as ImageView
            targetImageView.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.shape_square_rounded_16dp
                )
            )

            // 기존에 있던 TextView를 제거하고 새로 추가
            if (target.childCount > 1) {
                target.removeViewAt(1)
            }
            val overlayTextView = TextView(this).apply {
                text = "" // 초기 텍스트 설정
            }
            target.addView(overlayTextView, 1)
        }

        isFailDialogShown = false

        blockVisibility(binding.ibGameplayBtn, binding.ibGamestopBtn)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.ibGamestoryMsg.visibility = View.GONE
            binding.ibGamestoryMsgTxt.visibility = View.GONE
            blockVisibility(binding.ibGamestoryOff, binding.ibGamestoryOn)
        }, 10000)  // 10초 후 메시지 사라짐
    }

    private fun addBlock(block: BlockDTO) {
        when (block.blockType) {
            resources.getString(R.string.block_type_normal) -> {
                // FrameLayout 생성
                val newBlock = FrameLayout(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    id = basicBlockId
                    basicBlockId += 1
                }

                // ImageView 추가
                val imageView = ImageView(this).apply {
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

                    if (block.blockDescript == "부채질 하기") {
                        setImageResource(R.drawable.iv_gameblock_basic_type3)
                    }
                }

                // TextView 추가
                val textView = TextView(this).apply {
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
                binding.linearLayoutBlockList.addView(newBlock)

                dragSources.add(newBlock)
                newBlock.tag = block  // BlockDTO를 tag로 설정
            }

            resources.getString(R.string.block_type_repeat) -> {
                val newBlock = FrameLayout(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    id = repeatBlockId
                    repeatBlockId += 1
                }

                val imageView1 = ImageView(this).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                        setMargins(0, 3.dpToPx(), 0, 0)
                    }
                    setImageResource(R.drawable.ib_gameplay2_repeat)
                }
                val imageView2 = ImageView(this).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                        setMargins(0, 8.dpToPx(), 32.dpToPx(), 0) // 마진 설정
                    }
                    setImageResource(R.drawable.iv_repeat_cnt)
                }

                val editText = EditText(this).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                        setMargins(0, 0, 29.dpToPx(), 0) // 마진 설정
                    }
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    setText(block.repeat.toString())
                    setTextColor(ContextCompat.getColor(this@GameActivity, R.color.white))
                    textSize = 10.51f
                }

                val imageView3 = ImageView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                        setMargins(12.dpToPx(), 30.dpToPx(), 0, 0)
                    }
                    setImageResource(R.drawable.ib_gamplay2_sound) // 원하는 이미지 설정
                }

                newBlock.addView(imageView1)
                newBlock.addView(imageView2)
                newBlock.addView(imageView3)
                newBlock.addView(editText)

                imageView3.visibility = View.GONE

                // `LinearLayout`에 새 `FrameLayout` 추가
                binding.linearLayoutBlockList.addView(newBlock)
                dragSources.add(newBlock)
                newBlock.tag = block  // BlockDTO를 tag로 설정
            }
        }

    }

    // dp를 px로 변환하는 확장 함수
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }
}
