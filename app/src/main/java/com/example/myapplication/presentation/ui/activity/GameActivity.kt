package com.example.myapplication.presentation.ui.activity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityGameBinding

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.DRAG_FLAG_GLOBAL
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.DragStartHelper
import androidx.draganddrop.DropHelper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.data.mapper.toBlockDTO
import com.example.myapplication.data.mapper.toBlockDTOList
import com.example.myapplication.data.repository.remote.response.quiz.Answer
import com.example.myapplication.data.repository.remote.response.quiz.Question
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.viewmodel.ChapterViewModel
import com.example.myapplication.presentation.viewmodel.CharacterViewModel
import com.example.myapplication.presentation.viewmodel.QuizViewModel
import com.example.myapplication.presentation.widget.extention.loadCropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameActivity : BaseActivity<ActivityGameBinding>(R.layout.activity_game), GameInterface, FireConditionInterface {

    private var targetBlockMap = mutableMapOf<Int, Int?>()

    private val dragSources = mutableListOf<View>()
    private var basicBlockId = 1 // 생성되는 블록 아이디 - 블록 색 지정을 위해 만든 변수
    private var curGameId = 2
    private var gameId = 0
    private var chapterId = 2

    private lateinit var isQuizClearedViewModel: QuizViewModel
    private lateinit var isChapterClearedViewModel: ChapterViewModel
    private lateinit var getRandomCactusViewModel: CharacterViewModel

    private var hint = ""
    private var moomooMsg = ""
    private var question: List<Question> = mutableListOf()
    private var answer: List<Answer> = mutableListOf()

    private var isNextGame: Boolean = false // 다음 게임 넘어가는지 여부 판단
        set(value) {
            if (field != value) { // 값이 변경된 경우에만 업데이트
                field = value
                setLayout() // 레이아웃 초기화 호출
            }
        }

    private var moveXCnt = 0
    private var moveYCnt = 0
    private var moveWay = MutableList(10) { 0 }

    private var isRepeat = false
    private var repeatIdx: Int = -1
    private val dropTargets by lazy {
        GameDropTarget.getAllViews(binding)
    }

    private val successDialogComment by lazy {
        SuccessDialogComment.getAllComments()
    }

    override fun setLayout() {
        curGameId = intent.getIntExtra("game id", -1) // game id == quiz id, 챕터아이디1~2 퀴즈1~7
        observeLifeCycle()
        initViewModel()
        initBlock()
        initGame()
        gameFunction(binding)
        setupDropTargets(dropTargets, this, targetBlockMap, dragSources)

        isQuizClearedViewModel.quizDistinct(curGameId)
    }

    override fun initViewModel() {
        isQuizClearedViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        isChapterClearedViewModel = ViewModelProvider(this)[ChapterViewModel::class.java]
        getRandomCactusViewModel = ViewModelProvider(this)[CharacterViewModel::class.java]
    }

    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                isQuizClearedViewModel.quizDistinct.collectLatest {
                    when (it.result.code) {
                        200 -> {
                            gameId = it.payload?.quizId!!
                            hint = it.payload?.hint.toString()
                            moomooMsg = it.payload?.message.toString()
                            question = it.payload?.questions!!
                            answer = it.payload?.answers!!

                            binding.ivGameHintTxt.text = hint
                            binding.ibGamestoryMsgTxt.text = moomooMsg

                            initBlock()
                            setupDragSources(dragSources)
                        }
                    }
                }
            }
        }

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.CREATED) {
//                getRandomCactusViewModel.getRandomCactus.collectLatest {
//                    when (it.result.code) {
//                        200 -> {
//                            val randomCactus = it.payload?.cactusType
//                        }
//                    }
//                }
//            }
//        }

    }

    // init ---------------------------------------------------------
    override fun initBlock() {
        clearDragTargets(binding)
        question.toBlockDTOList().map {
            addBlock(it)
        }
    }

    override fun initGame() {
        initStory()
        initCharacterMove()
        runOnUiThread {
            initCharacter(curGameId, binding)
        }

        // 배경 설정
        setupGameBackground(curGameId)

        // 블록 초기화
        initDragBlock()
        initRepeatBlock()

        blockVisibility(binding.ibGameplayBtn, binding.ibGamestopBtn)
        setUpStory()
    }

    private fun initCharacterMove() {
        binding.ivGameCharacter.bringToFront() // 게임 캐릭터를 최상단으로 설정
        moveXCnt = 0
        moveYCnt = 0
        moveWay = MutableList(10) { 0 }
    }

    private fun initDragBlock() {
        targetBlockMap = mutableMapOf()
        dragSources.forEach { it.visibility = View.VISIBLE }
    }

    private fun initRepeatBlock() {
        dropTargets.forEach { target ->
            resetFrameLayoutVisibility(target)
            if (target is FrameLayout) {
                initFrameLayout(target)
            } else {
                Log.e("DropTarget", "Target is not a FrameLayout: ${target.id}")
            }
        }
    }

    private fun setUpStory() {
        Handler(Looper.getMainLooper()).postDelayed({
            setVisibilityForViews(
                visibleViews = emptyList(),
                hiddenViews = listOf(
                    binding.ibGamestoryMsg,
                    binding.ibGamestoryMsgTxt
                )
            )

            blockVisibility(binding.ibGamestoryOff, binding.ibGamestoryOn)
        }, 10000)  // 10초 후 메시지 사라짐
    }

    private fun setVisibilityForViews(visibleViews: List<View>, hiddenViews: List<View>) {
        visibleViews.forEach { it.visibility = View.VISIBLE }
        hiddenViews.forEach { it.visibility = View.GONE }
    }

    // 게임 ID에 따른 배경 설정
    private fun setupGameBackground(gameId: Int) {
        when (gameId) {
            3, 4 -> {
                setVisibilityForViews(
                    visibleViews = emptyList(),
                    hiddenViews = listOf(
                        binding.ivGameWay2,
                        binding.ivGameGum,
                        binding.ivGameWay3,
                        binding.ivGameFire,
                        binding.ivGameWay4
                    )
                )
            }
            5 -> {
                setVisibilityForViews(
                    visibleViews = listOf(
                        binding.ivGameWay2,
                        binding.ivGameGum
                    ),
                    hiddenViews = listOf(
                        binding.ivGameWay3,
                        binding.ivGameFire,
                        binding.ivGameWay4
                    )
                )
            }
            6 -> {
                setVisibilityForViews(
                    visibleViews = listOf(
                        binding.ivGameWay3,
                        binding.ivGameFire
                    ),
                    hiddenViews = listOf(
                        binding.ivGameWay,
                        binding.ivGameWay2,
                        binding.ivGameGum,
                        binding.ivGameFan,
                        binding.ivGameWay4
                    )
                )
            }
            else -> {
                setVisibilityForViews(
                    visibleViews = listOf(
                        binding.ivGameWay4,
                        binding.ivGameFire
                    ),
                    hiddenViews = listOf(
                        binding.ivGameWay,
                        binding.ivGameWay2,
                        binding.ivGameGum,
                        binding.ivGameWay3
                    )
                )
            }
        }
    }

    private fun resetFrameLayoutVisibility(target: View) {
        listOf(
            R.id.ib_biginner_game1_space1 to ImageView::class.java,
            R.id.ib_biginner_game1_space2 to ImageView::class.java,
            R.id.ib_gamestop_btn to ImageView::class.java,
            R.id.ib_gameplay_btn to EditText::class.java,
            R.id.ib_game_state_done to TextView::class.java
        ).forEach { (tagId, viewClass) ->
            (target.getTag(tagId) as? View)?.takeIf { viewClass.isInstance(it) }?.apply {
                visibility = View.GONE
            }
        }
    }

    private fun initFrameLayout(frameLayout: FrameLayout) {
        // 첫 번째 자식이 ImageView인지 확인 후 이미지 초기화
        (frameLayout.getChildAt(0) as? ImageView)?.setImageDrawable(
            ContextCompat.getDrawable(frameLayout.context, R.drawable.shape_square_rounded_16dp)
        )

        // 기존 TextView 제거 및 새 TextView 추가
        if (frameLayout.childCount > 1) {
            frameLayout.removeViewAt(1)
        }
        val overlayTextView = TextView(frameLayout.context).apply {
            text = "" // 초기 텍스트 설정
        }
        frameLayout.addView(overlayTextView, 1)
    }

    override fun addBlock(block: BlockDTO) {
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
                binding.linearLayoutBlockGameList.addView(newBlock)

                dragSources.add(newBlock)
                newBlock.tag = block  // BlockDTO를 tag로 설정
            }

            resources.getString(R.string.block_type_repeat) -> {
                val newBlock = FrameLayout(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
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
                    setImageResource(R.drawable.iv_gameblock_basic_type2) // 원하는 이미지 설정
                }

                newBlock.addView(imageView1)
                newBlock.addView(imageView2)
                newBlock.addView(imageView3)
                newBlock.addView(editText)

                imageView3.visibility = View.GONE

                // `LinearLayout`에 새 `FrameLayout` 추가
                binding.linearLayoutBlockGameList.addView(newBlock)
                dragSources.add(newBlock)
                newBlock.tag = block  // BlockDTO를 tag로 설정
            }
        }

    }

    private fun gameFunction(binding: ActivityGameBinding) {
        // 각종 버튼들 처리
        binding.ibGameplayBtn.setOnClickListener {
            binding.ibGameplayBtn.setOnClickListener {
                repeatAddMoveWay()
                blockVisibility(binding.ibGamestopBtn, binding.ibGameplayBtn)

                characterMove()
            }
        }

        binding.ibGameplayExitBtn.setOnClickListener {
            showExitDialog()
        }

        binding.ibGamestoryOn.setOnClickListener {
            onStoryState(false)
        }
        binding.ibGamestoryOff.setOnClickListener {
            onStoryState(true)
        }

        binding.ibBulbBtn.setOnClickListener {
            binding.ibBulbBtn.isSelected = !binding.ibBulbBtn.isSelected
            if (binding.ibBulbBtn.isSelected) {
                // 힌트 보여주기
                binding.ivGameHint.visibility = View.VISIBLE
                binding.ivGameHintTxt.visibility = View.VISIBLE
            } else {
                binding.ivGameHint.visibility = View.GONE
                binding.ivGameHintTxt.visibility = View.GONE
            }
        }
    }

    private fun repeatAddMoveWay() {
        if (repeatIdx != -1 && isRepeat) {
            Log.d("repeat index", repeatIdx.toString())
            val repeatEditText = dropTargets[repeatIdx]?.getTag(R.id.ib_gameplay_btn) as? EditText
            val targetTextView = dropTargets[repeatIdx].getTag(R.id.ib_game_state_done) as? TextView
            var tempStr = mappingStrToResourceId(targetTextView?.text.toString())

            if (repeatEditText?.text.toString().toInt() > 0 && tempStr != null) {
                for (i in 0 until repeatEditText?.text.toString().toInt() - 1) {
                    moveWay.add(repeatIdx, tempStr)
                }
            }
            isRepeat = false
        }
    }

    private fun onStoryState(isState: Boolean) {
        if (isState) {
            setVisibilityForViews(
                visibleViews = listOf(
                    binding.ibGamestoryImg,
                    binding.ibGamestoryTxt,
                    binding.ibGamestoryMsg,
                    binding.ibGamestoryMsgTxt,
                    binding.ibGamestoryOn
                ),
                hiddenViews = listOf(binding.ibGamestoryOff)
            )
        } else {
            setVisibilityForViews(
                visibleViews = listOf(binding.ibGamestoryOff),
                hiddenViews = listOf(
                    binding.ibGamestoryImg,
                    binding.ibGamestoryTxt,
                    binding.ibGamestoryMsg,
                    binding.ibGamestoryMsgTxt,
                    binding.ibGamestoryOn
                )
            )
        }
    }

    private fun initStory() {
        Handler(Looper.getMainLooper()).postDelayed({
            setVisibilityForViews(
                visibleViews = listOf(binding.ibGamestoryOff),
                hiddenViews = listOf(
                    binding.ibGamestoryImg,
                    binding.ibGamestoryTxt,
                    binding.ibGamestoryMsg,
                    binding.ibGamestoryMsgTxt,
                    binding.ibGamestoryOn
                )
            )
        }, 10000)  // 10초 후 메시지 사라짐
    }

    // drag and drop ------------------------------------------------
    override fun handleImageDrop(target: View, dragId: Int, dropId: Int) {
        targetBlockMap[dropId] = dragId
        dragSources[dragId].visibility = View.GONE

        val draggedBlock = dragSources[dragId] as FrameLayout
        val blockDTO = draggedBlock.tag as? BlockDTO
        val blockType = blockDTO?.blockType
        val blockMove = blockDTO?.blockDescript

        when (blockType) {
            resources.getString(R.string.block_type_normal) -> {
                // 드래그된 View (FrameLayout)에서 ImageView와 TextView를 가져옴
                val draggedImageView = draggedBlock.getChildAt(0) as ImageView
                val draggedTextView = draggedBlock.getChildAt(1) as TextView

                if (target is FrameLayout) {
                    // 기존에 "repeat" 블록이 있는지 확인
                    val repeatImageView = target.getTag(R.id.ib_gamestop_btn) as? ImageView
                    if (repeatImageView != null) {
                        // repeat 블록이 이미 존재하면 newImageView3의 visibility를 VISIBLE로 변경
                        repeatIdx = dropId
                        repeatImageView.visibility = View.VISIBLE
                        if (target.childCount > 1) {
                            target.removeViewAt(1)
                        }
                        // TextView를 target에 새로 추가
                        val overlayTextView = TextView(this).apply {
                            text = draggedTextView!!.text
                            textSize = 12f
                            setTextColor(ContextCompat.getColor(this@GameActivity, R.color.white))
                            setPadding(45, 90, 0, 0)
                        }
                        target.setTag(R.id.ib_game_state_done, overlayTextView)
                        target.addView(overlayTextView, 1)
                        overlayTextView.bringToFront()
                        overlayTextView.invalidate()
                        target.requestLayout()
                    } else {
                        // 기존 ImageView를 target에 덮어씌우기
                        val targetImageView = target.getChildAt(0) as ImageView
                        targetImageView.setImageDrawable(draggedImageView.drawable)

                        // 기존 TextView가 있다면 제거
                        if (target.childCount > 1) {
                            target.removeViewAt(1)
                        }

                        // TextView를 target에 새로 추가
                        val overlayTextView = TextView(this).apply {
                            text = draggedTextView!!.text
                            textSize = 12f
                            setTextColor(ContextCompat.getColor(this@GameActivity, R.color.white))
                            setPadding(20, 25, 0, 0)
                        }
                        target.addView(overlayTextView, 1)
                    }
                }
            }

            resources.getString(R.string.block_type_repeat) -> {
                isRepeat = true

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
                val draggedImageView2 = draggedBlock.getChildAt(1) as ImageView
                val draggedImageView3 = draggedBlock.getChildAt(2) as ImageView
                val draggedEditText = draggedBlock.getChildAt(3) as EditText

                if (target is FrameLayout) {
                    // 기존 EditText가 있다면 제거
                    if (target.childCount > 2) {
                        target.removeViewAt(2)
                    }

                    // newImageView1 추가
                    val newImageView1 = ImageView(this).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, 0, 0, 0)
                        }
                        setImageDrawable(draggedImageView1.drawable)
                    }
                    target.addView(newImageView1)
                    target.setTag(R.id.ib_biginner_game1_space1, newImageView1)

                    // newImageView2 추가
                    val newImageView2 = ImageView(this).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(35, 15, 0, 0)
                        }
                        setImageDrawable(draggedImageView2.drawable)
                        alpha = 0.9f
                    }
                    target.addView(newImageView2)
                    target.setTag(R.id.ib_biginner_game1_space2, newImageView2)

                    // newImageView3 추가
                    val newImageView3 = ImageView(this).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(35, 75, 0, 0)
                        }
                        setImageDrawable(draggedImageView3.drawable)
                        alpha = 0.8f
                        visibility = View.GONE // 기본값을 GONE으로 설정
                    }
                    target.addView(newImageView3)
                    target.setTag(R.id.ib_gamestop_btn, newImageView3) // newImageView3를 tag로 저장

                    // EditText 추가
                    val newEditText = EditText(this).apply {
                        setText(draggedEditText.text)
                        setTextColor(ContextCompat.getColor(this@GameActivity, R.color.white))
                        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                        textSize = 10.51f
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(30, 0, 0, 0)
                        }
                        setPadding(30, 0, 0, 0)
                    }
                    target.addView(newEditText)
                    target.setTag(R.id.ib_gameplay_btn, newEditText)
                }

            }

            else -> {
                // 블록 타입이 정의되지 않았을 경우 처리
                Log.e("block type error", "블록 타입이 정해지지 않았습니다.")
            }

        }

        var newdropId: Int
        if (repeatIdx == dropId) {
            newdropId = dropId + 1
        } else {
            newdropId = dropId
        }
        handleBlockMove(blockMove!!, newdropId, dropId)

    }

    private fun mappingStrToResourceId(string: String): Int {
        val resourceMap = mapOf(
            resources.getString(R.string.game_move_straight) to R.string.game_move_straight,
            resources.getString(R.string.game_move_up) to R.string.game_move_up,
            resources.getString(R.string.game_move_down) to R.string.game_move_down,
            resources.getString(R.string.game_repeat) to R.string.game_repeat,
            resources.getString(R.string.game_fanning) to R.string.game_fanning
        )

        return resourceMap[string] ?: R.string.game_wave
    }

    private fun handleBlockMove(blockMove: String, newdropId: Int, dropId: Int) {
        val move = mappingStrToResourceId(blockMove)
        if (move != null) {
            if (move == R.string.game_repeat) {
                moveWay[dropId] = move
                repeatIdx = dropId
            } else {
                moveWay[newdropId] = move
            }
        } else {
            moveWay[newdropId] = -1
        }
    }

    // check success ------------------------------------------------
    private fun checkSuccess() {
        val correctBlockOrder = getCorrectBlockOrder()
        val isSuccess = isMoveCorrect(correctBlockOrder)

        if (isSuccess) {
            handleSuccess()
        } else {
            showFailDialog()
        }
    }

    private fun getCorrectBlockOrder(): List<Int> {
        val correctBlock = answer.map { it.toBlockDTO().blockDescript }
        val correctBlockOrder = mutableListOf<Int>()
        for (cbo in correctBlock) {
            val resourceId = mappingStrToResourceId(cbo)
            correctBlockOrder.add(resourceId) // 변환된 resourceId를 리스트에 추가
        }
        return correctBlockOrder
    }

    private fun isMoveCorrect(correctBlockOrder: List<Int>): Boolean {
        for (mv in moveWay) {
            Log.d("test move way", mv.toString())
        }
        for (i in correctBlockOrder.indices) {
            if (moveWay[i] != correctBlockOrder[i]) {
                return false
            }
        }
        return true
    }

    private fun handleSuccess() {
        isQuizClearedViewModel.postQuizClear(curGameId)
        if (curGameId == 7) { // 마지막 퀴즈이면 챕터 클리어 POST
            Log.d("okhttp","클리어")
            isChapterClearedViewModel.postChapterClear(chapterId)
        }
        showSuccessDialog()
    }


    @SuppressLint("InflateParams")
    private fun showSuccessDialog() {
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

        title.text = successDialogComment[gameId - 3].first
        subTitle.text = successDialogComment[gameId - 3].second

        stopBtn.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        //****
        nextBtn.setOnClickListener {
            val intent = Intent(this, QuizClearActivity::class.java)
            intent.putExtra("game2Activity", true)
            startActivity(intent)
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

        val failTitle = dialogView.findViewById<TextView>(R.id.tv_gameplay_fail_title)
        val failSubTitle = dialogView.findViewById<TextView>(R.id.tv_gameplay_fail_subtitle)
        val retryBtn = dialogView.findViewById<Button>(R.id.btn_dialog_biginner_quiz_fail)

        failTitle.text = "앗 다시 한 번 도전해볼래?"
        failSubTitle.text = "조금만 더 힘을 내봐!"
        isNextGame = false

        retryBtn.setOnClickListener {
            initGame()
            dialog.dismiss()  // 다이얼로그 닫기
        }
        // 다이얼로그 보여주기
        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun showExitDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_success, null)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)

        val dialog = dialogBuilder.create()

        val title = dialogView.findViewById<TextView>(R.id.dialog_button_two_title)
        val subTitle = dialogView.findViewById<TextView>(R.id.dialog_button_two_subtitle)
        val stopBtn = dialogView.findViewById<Button>(R.id.dialog_button_stop)
        val nextBtn = dialogView.findViewById<Button>(R.id.dialog_button_next_step)

        title.text = "정말 그만두시겠어요?"
        subTitle.text = "그만하면 과정을 저장할 수 없어요\uD83E\uDD72"
        stopBtn.text = "그만하기"
        nextBtn.text = "이어서 하기"

        stopBtn.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        nextBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show() // 다이얼로그 표시
    }

    // else function -------------------------------------------------------------------------------


    // dp를 px로 변환하는 확장 함수
    override fun Int.dpToPx(): Int {
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

        fun moveStep(index: Int) {
            if (index >= moveWay.size) {
                checkSuccess() // 모든 이동이 끝난 후 성공 여부 확인
                return
            }

            // 현재 이동 방향
            val move = moveWay[index]
            Log.d("move way test", move.toString())
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
                R.string.game_repeat -> {
                    deltaX = 0f
                    deltaY = 0f
                    moveStep(index + 1)
                }
                R.string.game_fanning -> {
                    deltaX = 0f
                    deltaY = 0f
                    if (isFireCondition(this, curGameId, moveWay)) {
                        handleFireCondition(this)
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.ivGameFan.visibility = View.GONE
                        moveStep(index + 1)
                    }, 1000)
                }
                else -> {
                    deltaX = 0f
                    deltaY = 0f
                    moveStep(index + 1) // 아무 블록이 놓여있지 않을 경우 다음 블록 처리
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
}
