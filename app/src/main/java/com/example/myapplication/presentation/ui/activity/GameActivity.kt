package com.example.myapplication.presentation.ui.activity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityGameBinding

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
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.DragStartHelper
import androidx.draganddrop.DropHelper
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.widget.extention.loadCropImage
import org.w3c.dom.Text

class GameActivity : BaseActivity<ActivityGameBinding>(R.layout.activity_game) {

    private var isFailDialogShown = false
    private var targetBlockMap = mutableMapOf<Int, Int?>()
    private var isExit = false //나가기 버튼 클릭했는지 여부 판단
    private var isDialogShown = false // 다이얼로그 표시 상태 플래그

    private val dragSources = mutableListOf<View>()
    private var basicBlockId = 1 // 생성되는 블록 아이디 - 블록 색 지정을 위해 만든 변수
    private var repeatBlockId = 1 // 생성되는 블록 아이디

    private var isNextGame: Boolean = false // 다음 게임 넘어가는지 여부 판단
        set(value) {
            if (field != value) { // 값이 변경된 경우에만 업데이트
                field = value
                setLayout() // 레이아웃 초기화 호출
            }
        }
    private var moveXCnt = 0
    private var moveYCnt = 0
    private var moveWay = mutableListOf<Int>()

    private val dropTargets by lazy {
        mutableListOf(
            binding.ibBiginnerGame1Space1,
            binding.ibBiginnerGame1Space2,
            binding.ibBiginnerGame1Space3,
            binding.ibBiginnerGame1Space4,
            binding.ibBiginnerGame1Space5,
            binding.ibBiginnerGame1Space6
        )
    }

    private val backgroundImg by lazy {
        listOf(
            R.drawable.iv_biginner_game1_image,
            R.drawable.iv_biginner_game2_image,
            R.drawable.iv_candy_game_image
        )
    }

    private val successDialogComment by lazy {
        listOf(
            Pair("완벽한 아침이야!", "상쾌한 아침을 만들어줘서 고마워 :)"),
            Pair("우와, 멋지다!", "파도 소리를 듣게 해줘서 고마워 :)"),
            Pair("너무 달콤하다!", "사탕을 먹을 수 있게 도와줘서 고마워 :)"),
            Pair("우와 또 사탕이다!", "너무 맛있다 ㅎㅎ  도와줘서 고마워 :)"),
            Pair("휴~ 무사히 껌을 피했어!", "도와줘서 고마워 :)"),
            Pair("우와 불이 무사히 꺼졌어!", "도와줘서 고마워 :)"),
        )
    }

    override fun setLayout() {
        initBlock()
        initGame()
        gameFunction()
        setupDragSources()
        setupDropTargets()
    }

    // init ---------------------------------------------------------
    private fun initBlock() {
        clearBlocks()
        var gameId = intent.getIntExtra("game id", -1)
        Log.d("game id test", gameId.toString())
        if (gameId != 2 && isNextGame) gameId += 1 // 다음 게임으로 전환되도록

        when(gameId) {
            2 -> {
                if (!isNextGame) {
                    addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "준비하기", 0))
                    addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "일어나기", 0))
                    addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "세수하기", 0))
                    addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "아침먹기", 0))
                }
                else {
                    addBlock(BlockDTO(resources.getString(R.string.block_type_repeat), "번 반복하기", 3))
                    addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "파도 소리 재생", 0))
                }
            }
            3 -> {
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
//                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_up), 0))
//                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_down), 0))
            }
            4 -> {
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
            }
            5 -> {
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_up), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_down), 0))
            }
        }
//        val blockCnt = dragSources.count()
//
//        // dropTargets에서 blockCnt만큼 남기고 뒤에서부터 제거
//        while (dropTargets.size > blockCnt) {
//            dropTargets.removeAt(dropTargets.size - 1)
//        }
    }

    private fun initGame() {
        binding.ivGameCharacter.bringToFront() // 게임 캐릭터가 무조건 최상단에 오도록

        var gameId = intent.getIntExtra("game id", -1)
        isExit = false
        isDialogShown = false

        // 캐릭터 관련
        moveXCnt = 0
        moveYCnt = 0
        moveWay.clear()
        if (gameId != 2 && isNextGame) gameId += 1
        Log.d("game id test", gameId.toString())
        initCharacter(gameId)

        // 배경 설정
        if (gameId == 2) {
            // 초심자의 섬
            if (!isNextGame) backgroundVisibility(backgroundImg[0])
            else backgroundVisibility(backgroundImg[1])

            binding.ivGameCharacter.visibility = View.GONE
            binding.ivGameWay.visibility = View.GONE
            binding.ivGameCandy.visibility = View.GONE

            binding.ivGameWay2.visibility = View.GONE
            binding.ivGameGum.visibility = View.GONE
        } else {
            // 사탕의 섬
            backgroundVisibility(backgroundImg[2])
            if (gameId == 3 || gameId == 4) {
                binding.ivGameWay2.visibility = View.GONE
                binding.ivGameGum.visibility = View.GONE
            }
            else {
                binding.ivGameWay2.visibility = View.VISIBLE
                binding.ivGameGum.visibility = View.VISIBLE
            }
        }

        targetBlockMap = mutableMapOf()
        for (dragSource in dragSources) {
            dragSource.visibility = View.VISIBLE
        }

        dropTargets.forEach { target ->
            // 기존 ImageView 리셋
            val targetImageView = target.getChildAt(0) as ImageView
            targetImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.shape_square_rounded_16dp))

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

    private fun initCharacter(game: Int) {
        // 캐릭터 다시 원래 위치로
        when (game) {
            3 -> {
                val view = binding.ivGameCharacter
                view.translationX = 0f  // X 좌표 초기화
                view.translationY = 0f  // Y 좌표 초기화
                view.invalidate() // 강제로 뷰 갱신
            }
            4, 5 -> {
                val character = binding.ivGameCharacter
                runOnUiThread {
                    character.translationX = -320f
                    character.translationY = 0f
                }

                val candy = binding.ivGameCandy
                runOnUiThread {
                    candy.translationX = 200f
                    candy.translationY = 0f
                }
            }
        }
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

                    if (block.blockDescript == resources.getString(R.string.game_fanning)) {
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

    private fun blockVisibility(visibleBlock: ImageButton, goneBlock: ImageButton) {
        visibleBlock.visibility = View.VISIBLE
        goneBlock.visibility = View.GONE
    }

    private fun clearBlocks() {
        binding.linearLayoutBlockList.removeAllViews()
    }

    private fun gameFunction() {
        // 각종 버튼들 처리
        binding.ibGameplayBtn.setOnClickListener {
            blockVisibility(binding.ibGamestopBtn, binding.ibGameplayBtn)

            if (moveWay.isEmpty()) {
                checkSuccess()
            } else {
                characterMove()
            }
        }

        binding.ibGamestopBtn.setOnClickListener {
            blockVisibility(binding.ibGameplayBtn, binding.ibGamestopBtn)
        }
        binding.ibGameplayExitBtn.setOnClickListener {
            isExit = true
            showSuccessDialog(isExit)
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

    // drag and drop ------------------------------------------------

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
        val blockMove = BlockDTO?.blockDescript

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

        when (blockMove) {
            resources.getString(R.string.game_move_straight) -> {
                moveWay.add(R.string.game_move_straight)
            }
            resources.getString(R.string.game_move_up) -> {
                moveWay.add(R.string.game_move_up)
            }
            resources.getString(R.string.game_move_down) -> {
                moveWay.add(R.string.game_move_down)
            }
        }
    }

    // check success ------------------------------------------------
    private fun checkSuccess() {
        if (isDialogShown) return
        var gameId = intent.getIntExtra("game id", -1)
        if (isNextGame) gameId += 1
        initCharacter(gameId)

        var correctBlockOrder: List<Int>
        var success = true
        when (gameId) {
            2 -> {
                correctBlockOrder = listOf(1, 2, 3, 0)
                dropTargets.forEachIndexed { index, target ->
                    // 올바른 블록이 각 dropTarget에 들어왔는지 확인
                    val droppedBlock = targetBlockMap[index]
                    if (droppedBlock == null) {
                        Log.d(TAG, "타겟 ${index + 1}에 블록이 드롭되지 않았습니다.")
                        success = false
                    } else if (droppedBlock != correctBlockOrder[index]) {
                        Log.d(TAG, "타겟 ${index + 1}에 잘못된 블록이 드롭되었습니다. 예상: ${correctBlockOrder[index]}, 실제: $droppedBlock")
                        success = false
                    }
                }
            }
            3 -> {
                correctBlockOrder = listOf(R.string.game_move_straight)
                if (moveWay != correctBlockOrder) {
                    success = false
                }
            }
            4 -> {
                correctBlockOrder = listOf(R.string.game_move_straight, R.string.game_move_straight, R.string.game_move_straight, R.string.game_move_straight)
                if (moveWay != correctBlockOrder) {
                    success = false
                }
            }
            else -> {
                correctBlockOrder = listOf(R.string.game_move_straight, R.string.game_move_down, R.string.game_move_straight, R.string.game_move_straight, R.string.game_move_up, R.string.game_move_straight)
                if (moveWay != correctBlockOrder) {
                    success = false
                }
            }
        }
        if (success) {
            isDialogShown = true
            // 성공 다이얼로그 출력
            showSuccessDialog(false)
            isNextGame = true
        } else {
            // 실패 다이얼로그 출력
            if (!isFailDialogShown) { // 실패 다이얼로그가 이미 표시되지 않았으면
                showFailDialog()
                isFailDialogShown = true
                if (!isNextGame) isNextGame = false
            }
        }
    }
    private fun showSuccessDialog(exit: Boolean) {
        val gameId = intent.getIntExtra("game id", -1)
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

        if (exit) {
            title.text = "정말 그만두시겠어요?"
            subTitle.text = "그만하면 과정을 저장할 수 없어요"
            nextBtn.text = "이어서 하기"

            stopBtn.setOnClickListener {
                dialog.dismiss()
                finish()
            }

            nextBtn.setOnClickListener {
                dialog.dismiss()
            }
        }

        else {
            if (gameId == 2) {
                if (!isNextGame) {
                    title.text = successDialogComment[0].first
                    subTitle.text = successDialogComment[0].second
                }
                else {
                    title.text = successDialogComment[1].first
                    subTitle.text = successDialogComment[1].second
                }
            } else {
                if (!isNextGame) {
                    title.text = successDialogComment[gameId - 1].first
                    subTitle.text = successDialogComment[gameId - 1].second
                }
                else {
                    title.text = successDialogComment[gameId].first
                    subTitle.text = successDialogComment[gameId].second
                }
            }

            stopBtn.setOnClickListener {
                isDialogShown = false
                dialog.dismiss()
                finish()
            }

            nextBtn.setOnClickListener {
                isDialogShown = false
                dialog.dismiss()
                initGame()
            }
        }
        Log.d("game id list", gameId.toString())
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

        val failTitle = dialogView.findViewById<TextView>(R.id.tv_gameplay_fail_title)
        val failSubTitle = dialogView.findViewById<TextView>(R.id.tv_gameplay_fail_subtitle)
        val retryBtn = dialogView.findViewById<Button>(R.id.btn_dialog_biginner_quiz_fail)

        failTitle.text = "앗 다시 한 번 도전해볼래?"
        failSubTitle.text = "조금만 더 힘을 내봐!"

        retryBtn.setOnClickListener {
            initGame()
            dialog.dismiss()  // 다이얼로그 닫기
        }
        // 다이얼로그 보여주기
        dialog.show()
    }

    // else function -------------------------------------------------------------------------------


    // dp를 px로 변환하는 확장 함수
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }

    private fun moveAnimation(deltaX: Float, deltaY: Float, onComplete: () -> Unit = {}) {
        val view = binding.ivGameCharacter // 이동할 뷰
        val targetX = view.translationX + (180 * deltaX)
        val targetY = view.translationY + (180 * deltaY)

        var animationsCompleted = 0 // 완료된 애니메이션 카운터

        // X축 이동
        if (deltaX != 0f) {
            val animatorMoveX = ValueAnimator.ofFloat(view.translationX, targetX).apply {
                addUpdateListener { animation ->
                    view.translationX = animation.animatedValue as Float
                }
                addListener(onEnd = {
                    animationsCompleted++
                    if (animationsCompleted == 2) {
                        onComplete()
                    }
                })
            }
            animatorMoveX.duration = 500
            animatorMoveX.start()
        } else {
            animationsCompleted++
        }

        // Y축 이동
        if (deltaY != 0f) {
            val animatorMoveY = ValueAnimator.ofFloat(view.translationY, targetY).apply {
                addUpdateListener { animation ->
                    view.translationY = animation.animatedValue as Float
                }
                addListener(onEnd = {
                    animationsCompleted++
                    if (animationsCompleted == 2) {
                        onComplete()
                    }
                })
            }
            animatorMoveY.duration = 500
            animatorMoveY.start()
        } else {
            animationsCompleted++
        }
    }

    private fun characterMove() {
        var currentX = 0f // 현재 X 위치
        var currentY = 0f // 현재 Y 위치

        // 재귀적으로 이동 실행
        fun moveStep(index: Int) {
            if (index >= moveWay.size) {
                checkSuccess() // 모든 이동이 끝난 후 성공 여부 확인
                return
            }

            // 현재 이동 방향
            val move = moveWay[index]
            val deltaX: Float
            val deltaY: Float

            when (move) {
                R.string.game_move_straight -> {
                    deltaX = 1f
                    deltaY = 0f
                }
                R.string.game_move_up -> {
                    deltaX = 0f
                    deltaY = -1f
                }
                R.string.game_move_down -> {
                    deltaX = 0f
                    deltaY = 1f
                }
                else -> {
                    deltaX = 0f
                    deltaY = 0f
                }
            }

            // 상대적 이동 실행
            moveAnimation(deltaX, deltaY) {
                // 현재 위치 업데이트
                currentX += deltaX
                currentY += deltaY
                moveStep(index + 1) // 다음 이동 실행
            }
        }

        moveStep(0) // 첫 번째 이동 실행
    }


    // 배경 지정
    private fun backgroundVisibility(background: Int) {
        binding.ivGameBackground.loadCropImage(background)
    }
}

