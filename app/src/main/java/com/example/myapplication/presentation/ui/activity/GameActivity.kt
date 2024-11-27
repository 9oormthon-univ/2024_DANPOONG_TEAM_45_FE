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
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.DragStartHelper
import androidx.draganddrop.DropHelper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.viewmodel.ChapterViewModel
import com.example.myapplication.presentation.viewmodel.CharacterViewModel
import com.example.myapplication.presentation.viewmodel.QuizViewModel
import com.example.myapplication.presentation.widget.extention.loadCropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameActivity : BaseActivity<ActivityGameBinding>(R.layout.activity_game), GameInterface {

    private var isFailDialogShown = false
    private var targetBlockMap = mutableMapOf<Int, Int?>()
    private var isExit = false //나가기 버튼 클릭했는지 여부 판단
    private var isDialogShown = false // 다이얼로그 표시 상태 플래그

    private val dragSources = mutableListOf<View>()
    private var basicBlockId = 1 // 생성되는 블록 아이디 - 블록 색 지정을 위해 만든 변수
    private var repeatBlockId = 1 // 생성되는 블록 아이디
    private var curGameId = 2
    private var chapterId = 1

    private lateinit var isQuizClearedViewModel: QuizViewModel
    private lateinit var isChapterClearedViewModel: ChapterViewModel
    private lateinit var getRandomCactusViewModel: CharacterViewModel

    private var hint = ""
    private var moomooMsg = ""

    private var isNextGame: Boolean = false // 다음 게임 넘어가는지 여부 판단
        set(value) {
            if (field != value) { // 값이 변경된 경우에만 업데이트
                field = value
                setLayout() // 레이아웃 초기화 호출
            }
        }

    private var isFirstStage = true
        set(value) {
            Log.d("isFirstStage Debug", "isFirstStage 변경 전: $field, 변경 후: $value")
            if (field != value) {
                field = value
                Log.d("isFirstStage Debug", "initGame 호출")
                setLayout()
            }
        }

    private var moveXCnt = 0
    private var moveYCnt = 0
    private var moveWay = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    private var isRepeat = false
    private var repeatIdx: Int = -1
    private var draggedTextView: TextView? = null
    private val dropTargets by lazy {
        mutableListOf(
            binding.ibBiginnerGame1Space1,
            binding.ibBiginnerGame1Space2,
            binding.ibBiginnerGame1Space3,
            binding.ibBiginnerGame1Space4,
            binding.ibBiginnerGame1Space5,
            binding.ibBiginnerGame1Space6,
            binding.ibBiginnerGame1Space7
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
            Pair("우와 불이 무사히 꺼졌어!", "도와줘서 고마워 :)"),
        )
    }

    override fun setLayout() {
        curGameId = intent.getIntExtra("game id", -1) // game id == quiz id, 챕터아이디1~2 퀴즈1~7
        if (curGameId <= 2 && isFirstStage) {
            // 초심자의 섬
            chapterId = 1
        } else if (curGameId > 2) {
            // 사탕의 섬
            chapterId = 2
            isFirstStage = false
        }
        observeLifeCycle()
        initViewModel()
        initBlock()
        initGame()
        gameFunction(binding)
        setupDragSources(dragSources)
        setupDropTargets(dropTargets, this)

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
                            hint = it.payload?.hint.toString()
                            moomooMsg = it.payload?.message.toString()

                            binding.ivGameHintTxt.text = hint
                            binding.ibGamestoryMsgTxt.text = moomooMsg

                            if (it.payload?.quizId == 2) {
                                binding.ivGameHintTxt.text = "무무가 아침 일정을 잘 마치도록 도와줘\n일어나기 > 세수하기 > 아침먹기 > 준비하기\n순서로 부탁할게! 해줄 수 있지?"
                            }
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

        Log.d("로그","$curGameId")
        when(curGameId) {
            2 -> {
                if (isFirstStage) {
                    addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "준비하기", 0))
                    addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "일어나기", 0))
                    addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "세수하기", 0))
                    addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "아침먹기", 0))
                }
                else {
                    addBlock(BlockDTO(resources.getString(R.string.block_type_repeat), resources.getString(R.string.game_repeat), 3))
                    addBlock(BlockDTO(resources.getString(R.string.block_type_normal), "파도 소리 재생", 0))
                }
            }
            3 -> {
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
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

            6 -> {
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_up), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_fanning), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_down), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
            }
            7 -> {
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_straight), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_up), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_down), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_move_down), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_normal), resources.getString(R.string.game_fanning), 0))
                addBlock(BlockDTO(resources.getString(R.string.block_type_repeat), resources.getString(R.string.game_repeat), 0))
            }
        }
    }

    //위에 예시를 간소화 한 예시 함수
    private fun manageAddBlock(blockDTOList: List<BlockDTO>){
        blockDTOList.map {
            addBlock(it)
        }
    }

    override fun initGame() {
        binding.ivGameCharacter.bringToFront() // 게임 캐릭터가 무조건 최상단에 오도록

        isExit = false
        isDialogShown = false

        // 캐릭터 관련
        moveXCnt = 0
        moveYCnt = 0
        moveWay = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
//        if (curGameId != 2 && isNextGame) gameId += 1
//        Log.d("game id test", gameId.toString())
        runOnUiThread {
            initCharacter(curGameId, binding)
        }

        // 배경 설정
        if (curGameId == 2) {
            // 초심자의 섬
            if (isFirstStage) backgroundVisibility(backgroundImg[0])
            else backgroundVisibility(backgroundImg[1])

            binding.ivGameCharacter.visibility = View.GONE
            binding.ivGameWay.visibility = View.GONE
            binding.ivGameCandy.visibility = View.GONE
            binding.ivGameWay2.visibility = View.GONE
            binding.ivGameGum.visibility = View.GONE
            binding.ivGameWay3.visibility = View.GONE
            binding.ivGameFire.visibility = View.GONE
            binding.ivGameWay4.visibility = View.GONE
        } else {
            // 사탕의 섬
            backgroundVisibility(backgroundImg[2])
            if (curGameId == 3 || curGameId == 4) {
                binding.ivGameWay2.visibility = View.GONE
                binding.ivGameGum.visibility = View.GONE
                binding.ivGameWay3.visibility = View.GONE
                binding.ivGameFire.visibility = View.GONE
                binding.ivGameWay4.visibility = View.GONE
            }
            else if (curGameId == 5) {
                binding.ivGameWay2.visibility = View.VISIBLE
                binding.ivGameGum.visibility = View.VISIBLE
                binding.ivGameWay3.visibility = View.GONE
                binding.ivGameFire.visibility = View.GONE
                binding.ivGameWay4.visibility = View.GONE
            }
            else if (curGameId == 6){
                binding.ivGameWay.visibility = View.GONE
                binding.ivGameWay2.visibility = View.GONE
                binding.ivGameGum.visibility = View.GONE

                binding.ivGameWay3.visibility = View.VISIBLE
                binding.ivGameFire.visibility = View.VISIBLE
                binding.ivGameFan.visibility = View.GONE
                binding.ivGameWay4.visibility = View.GONE
            }
            else {
                binding.ivGameWay.visibility = View.GONE
                binding.ivGameWay2.visibility = View.GONE
                binding.ivGameGum.visibility = View.GONE
                binding.ivGameWay3.visibility = View.GONE

                binding.ivGameWay4.visibility = View.VISIBLE
                binding.ivGameFire.visibility = View.VISIBLE
            }
        }

        targetBlockMap = mutableMapOf()
        for (dragSource in dragSources) {
            dragSource.visibility = View.VISIBLE
        }

        dropTargets.forEach { target ->
            // 기존 ImageView 리셋
            val removeTarget1 = target.getTag(R.id.ib_biginner_game1_space1) as? ImageView
            val removeTarget2 = target.getTag(R.id.ib_biginner_game1_space2) as? ImageView
            val removeTarget3 = target.getTag(R.id.ib_gamestop_btn) as? ImageView
            val removeTarget4 = target.getTag(R.id.ib_gameplay_btn) as? EditText
            val removeTarget5 = target.getTag(R.id.ib_game_state_done) as? TextView

            removeTarget1?.visibility = View.GONE
            removeTarget2?.visibility = View.GONE
            removeTarget3?.visibility = View.GONE
            removeTarget4?.visibility = View.GONE
            removeTarget5?.visibility = View.GONE

            val targetImageView = target.getChildAt(0) as ImageView
            targetImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.shape_square_rounded_16dp))
            FrameLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            // 기존에 있던 TextView를 제거하고 새로 추가
            if (target.childCount >= 1) {
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

    override fun gameFunction(binding: ActivityGameBinding) {
        // 각종 버튼들 처리
        Log.d("Debug", "isFirstStage: $isFirstStage, isNextGame: $isNextGame, before: $curGameId")
        binding.ibGameplayBtn.setOnClickListener {
            if (repeatIdx != -1 && isRepeat) {
                Log.d("repeat index", repeatIdx.toString())
                val repeatEditText = dropTargets[repeatIdx]?.getTag(R.id.ib_gameplay_btn) as? EditText
                val targetTextView = dropTargets[repeatIdx].getTag(R.id.ib_game_state_done) as? TextView
                var tempStr = when (targetTextView?.text.toString()) { // null 체크
                    resources.getString(R.string.game_move_straight) -> R.string.game_move_straight
                    resources.getString(R.string.game_move_up) -> R.string.game_move_up
                    resources.getString(R.string.game_move_down) -> R.string.game_move_down
                    resources.getString(R.string.game_wave) -> R.string.game_wave
                    else -> R.string.game_repeat // 일단 부채질 반복은 ,, 고려하지 않았음
                }

                if (repeatEditText?.text.toString().toInt() > 0) {
                    for (i in 0 until repeatEditText?.text.toString().toInt() - 1) {
                        moveWay.add(repeatIdx, tempStr)
                    }
                }
                isRepeat = false
            }
            blockVisibility(binding.ibGamestopBtn, binding.ibGameplayBtn)

            if (moveWay.contains(R.string.game_wave) || moveWay.contains(R.string.game_wake) || moveWay.contains(R.string.game_breakfast) || moveWay.contains(R.string.game_wash) || moveWay.contains(R.string.game_practice)) {
                checkSuccess()
            } else {
                characterMove()
            }
        }

        binding.ibGameplayExitBtn.setOnClickListener {
            isExit = true
            showSuccessDialog(isExit)
        }
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
        val repeatImageView = target.getTag(R.id.ib_gamestop_btn) as? ImageView
//        if (repeatImageView != null && repeatIdx > 0) {
//            moveWay[dropId - 1] = R.string
//        }
        var newdropId: Int
        if (repeatIdx == dropId) {
            newdropId = dropId + 1
        } else {
            newdropId = dropId
        }
//        val draggedTextView = draggedBlock.getChildAt(1) as TextView
        handleBlockMove(blockMove!!, newdropId, dropId)

    }

    fun handleBlockMove(blockMove: String, newdropId: Int, dropId: Int) {
        val blockMoveMap = mapOf(
            resources.getString(R.string.game_move_straight) to R.string.game_move_straight,
            resources.getString(R.string.game_move_up) to R.string.game_move_up,
            resources.getString(R.string.game_move_down) to R.string.game_move_down,
            resources.getString(R.string.game_repeat) to R.string.game_repeat,
            resources.getString(R.string.game_fanning) to R.string.game_fanning,
            resources.getString(R.string.game_wake) to R.string.game_wake,
            resources.getString(R.string.game_wash) to R.string.game_wash,
            resources.getString(R.string.game_practice) to R.string.game_practice,
            resources.getString(R.string.game_breakfast) to R.string.game_breakfast,
            resources.getString(R.string.game_wave) to R.string.game_wave
        )

        val move = blockMoveMap[blockMove]
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
    override fun checkSuccess() {
        if (isDialogShown) return

        for (mv in moveWay) {
            Log.d("dfdfd", mv.toString())
        }

        var correctBlockOrder = listOf(0)
        var successCnt = 0
        when (curGameId) {
            2 -> {
                if (isFirstStage) {
                    correctBlockOrder = listOf(R.string.game_wake, R.string.game_wash, R.string.game_breakfast, R.string.game_practice)
                }
                else {
                    correctBlockOrder = listOf(R.string.game_wave, R.string.game_wave, R.string.game_repeat, R.string.game_wave)
                }
            }
            3 -> correctBlockOrder = listOf(R.string.game_move_straight)
            4 -> correctBlockOrder = listOf(
                R.string.game_move_straight,
                R.string.game_move_straight,
                R.string.game_move_straight,
                R.string.game_move_straight)
            5 -> correctBlockOrder = listOf(
                R.string.game_move_straight,
                R.string.game_move_down,
                R.string.game_move_straight,
                R.string.game_move_straight,
                R.string.game_move_up,
                R.string.game_move_straight)
            6 -> correctBlockOrder = listOf(
                R.string.game_move_straight,
                R.string.game_move_up,
                R.string.game_move_straight,
                R.string.game_fanning,
                R.string.game_move_straight,
                R.string.game_move_straight,
                R.string.game_move_down)
            7 -> correctBlockOrder = listOf(
                R.string.game_fanning,
                R.string.game_move_down,
                R.string.game_move_straight,
                R.string.game_move_straight,
                R.string.game_move_straight,
                R.string.game_move_straight,
                R.string.game_repeat,
                R.string.game_move_straight
            )
        }

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
            if (!isFirstStage) {
                isQuizClearedViewModel.postQuizClear(curGameId)
                if (curGameId == 2 || curGameId == 7) {
                    isChapterClearedViewModel.postChapterClear(chapterId)
                }
            }

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
    override fun showSuccessDialog(exit: Boolean) {
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
                if (isFirstStage) {
                    title.text = successDialogComment[gameId - 2].first
                    subTitle.text = successDialogComment[gameId - 2].second
                }
                else {
                    title.text = successDialogComment[gameId - 1].first
                    subTitle.text = successDialogComment[gameId - 1].second
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

            //****
            nextBtn.setOnClickListener {
                if (!isFirstStage) {
                    val intent = Intent(this, QuizClearActivity::class.java)
                    intent.putExtra("game2Activity", true)
                    startActivity(intent)
                    finish()
                } else {
                    isDialogShown = false
                    Log.d("isFirstStage Debug", "isFirstStage를 false로 설정합니다.")
                    isFirstStage = false  // 값을 명시적으로 업데이트
                    isNextGame = true
                    dialog.dismiss()
                    initGame()  // dismiss 이후 즉시 실행
                }
            }
        }
        Log.d("game id list", gameId.toString())
        // 다이얼로그 보여주기
        dialog.show()
    }

    override fun showFailDialog() {
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

    // else function -------------------------------------------------------------------------------


    // dp를 px로 변환하는 확장 함수
    override fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }

    fun moveAnimation(deltaX: Float, deltaY: Float, onComplete: () -> Unit = {}) {
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

    fun characterMove() {
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
                    if (isFireCondition()) {
                        handleFireCondition()
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


    // 배경 지정
    fun backgroundVisibility(background: Int) {
        binding.ivGameBackground.loadCropImage(background)
    }

    fun isFireCondition(): Boolean {
        // Fire가 발생할 조건을 정의
        if (curGameId == 6) {
            val fanBlockOrder = listOf(
                R.string.game_move_straight,
                R.string.game_move_up,
                R.string.game_move_straight,
                R.string.game_fanning
            )
            if (moveWay.size >= 4) {
                for (i: Int in 0..3) {
                    if (fanBlockOrder[i] != moveWay[i])
                        return false
                }
                return true
            } else {
                return false
            }
        }
        else {
            return (R.string.game_fanning == moveWay[0])
        }
    }

    private fun handleFireCondition() {
        // Fire 처리 로직
        blockVisibility(binding.ivGameFan, binding.ivGameFire)
    }
}
