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
import androidx.core.view.forEach
import androidx.draganddrop.DropHelper
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.viewmodel.QuizViewModel
import com.example.myapplication.presentation.widget.extention.loadCropImage
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text

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

    private lateinit var isQuizClearedViewModel: QuizViewModel

    private var isNextGame: Boolean = false // 다음 게임 넘어가는지 여부 판단
        set(value) {
            if (field != value) { // 값이 변경된 경우에만 업데이트
                field = value
                setLayout() // 레이아웃 초기화 호출
            }
        }

    private var isFirstStage = true
        set(value) {
            if (field != value) { // 값이 변경된 경우에만 업데이트
                field = value
                setLayout() // 레이아웃 초기화 호출
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

    private val moomooMessage by lazy {
        listOf(
            "무무가 아침 일정을 잘 마치도록 도와줘\n일어나기 > 세수하기 > 아침먹기 > 준비하기\n순서로 부탁할게! 해줄 수 있지?",
            "바다에서 파도 소리를 듣고 싶어! 파도는 \n일정한 주기로 밀려왔다가 사라진대 파도소리가 \n+3번 들리게 만들어줄 수 있어?",
            "사탕을 먹고 싶어! \n사탕을 먹을 수 있게 도와줄 수 있니?",
            "이번엔 사탕을 찾기가 더 어렵네 ㅠ\n사탕까지 갈 수 있게 도와줘!",
            "길 위에 끈적한 껌이 있어!\n껌을 밟지 않고 사탕을 얻을 수 있게 도와줘ㅠ",
            "이런... 사탕의 섬에 불이 났잖아!\n사탕이 다 녹기 전에 불을 끄고 사탕을 얻어야 해",
            "헉! 큰일이야... 사탕의 섬에 큰 불이 난 것 같아ㅠ\n불을 진압하고 사탕의 섬을 빠져나오자!"
        )
    }

    private val hintComment by lazy {
        listOf(
            "",
            "",
            "앞으로 가기 블럭을 한 번만 사용하세요",
            "앞으로 가기 블럭을 여러번 사용하세요",
            "앞으로 가기 블럭과 위/아래로 가기 \n블럭의 순서를 잘 조합해 껌을 피해보세요",
            "미션블럭을 활용해보세요\n부채질 하기 블럭을 사용할 수 있어요",
            "0번 반복하기 블럭을 사용해\n앞으로 나아가세요"
        )
    }

    override fun setLayout() {
        curGameId = intent.getIntExtra("game id", -1)
//        binding.ivGameHintTxt.text = hintComment[curGameId - 2]
//        binding.ibGamestoryMsgTxt.text = moomooMessage[curGameId - 2]
        initViewModel()
        initBlock()
        initGame()
        gameFunction()
        setupDragSources()
        setupDropTargets()
    }

    override fun initViewModel() {
        isQuizClearedViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
    }

    // init ---------------------------------------------------------
    override fun initBlock() {
        clearBlocks()

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

    override fun initGame() {
        if (!isFirstStage && isNextGame) {
            curGameId += 1
            Log.d("Debug", "After: curGameId = $curGameId")
            isNextGame = false
            initBlock()
        }
        binding.ivGameCharacter.bringToFront() // 게임 캐릭터가 무조건 최상단에 오도록

        isExit = false
        isDialogShown = false

        // 캐릭터 관련
        moveXCnt = 0
        moveYCnt = 0
        moveWay = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
//        if (curGameId != 2 && isNextGame) gameId += 1
//        Log.d("game id test", gameId.toString())
        initCharacter(curGameId)

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

    override fun initCharacter(game: Int) {
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

            6 -> {
                val character = binding.ivGameCharacter
                runOnUiThread {
                    character.translationX = -280f
                    character.translationY = 0f
                }

                val candy = binding.ivGameCandy
                runOnUiThread {
                    candy.translationX = 200f
                    candy.translationY = 0f
                }
            }
            7 -> {
                val character = binding.ivGameCharacter
                runOnUiThread {
                    character.translationX = -360f
                    character.translationY = -180f
                }

                val fire = binding.ivGameFire
                runOnUiThread {
                    fire.translationX = -580f
                    fire.translationY = 180f
                }

                val candy = binding.ivGameCandy
                runOnUiThread {
                    candy.translationX = 280f
                    candy.translationY = 0f
                }

                val fan = binding.ivGameFan
                runOnUiThread {
                    fan.translationX = -540f
                    fan.translationY = 0f
                }
            }
        }
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
                binding.linearLayoutBlockList.addView(newBlock)
                dragSources.add(newBlock)
                newBlock.tag = block  // BlockDTO를 tag로 설정
            }
        }

    }

    override fun blockVisibility(visibleBlock: View, goneBlock: View) {
        visibleBlock.visibility = View.VISIBLE
        goneBlock.visibility = View.GONE
    }

    override fun clearBlocks() {
        binding.linearLayoutBlockList.removeAllViews()
    }

    override fun gameFunction() {
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
                    for (i in 0..<repeatEditText?.text.toString().toInt() - 1) {
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
                binding.ivGameHintTxt.visibility = View.VISIBLE
            } else {
                binding.ivGameHint.visibility = View.GONE
                binding.ivGameHintTxt.visibility = View.GONE
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

    override fun setupDragSources() {
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

    override fun setupDropTargets() {
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
                    val dropTargetId = dropTargets.indexOf(target)

//                    // 현재 타겟에 이미 블록이 놓여 있는 경우 - 다시 제자리에 갖다놓기
//                    val previousBlockId = targetBlockMap[dropTargetId]
//                    if (previousBlockId != null) {
//                        val previousBlock = dragSources[previousBlockId]
//                        val previousBlockDTO = previousBlock.tag as? BlockDTO
//                        val previousBlockType = previousBlockDTO?.blockType
//
//                        // 이전에 놓인 블록이 repeat이면 제외
//                        if (previousBlockType != getString(R.string.block_type_repeat)) {
//                            dragSources[previousBlockId].visibility = View.VISIBLE
//                            removeBlockFromMoveWay(previousBlockId)
//                        }
//                    }

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
        when (blockMove) {
            resources.getString(R.string.game_move_straight) -> {
                moveWay[newdropId] = R.string.game_move_straight
            }
            resources.getString(R.string.game_move_up) -> {
                moveWay[newdropId] = R.string.game_move_up
            }
            resources.getString(R.string.game_move_down) -> {
                moveWay[newdropId] = R.string.game_move_down
            }
            resources.getString(R.string.game_repeat) -> {
                moveWay[dropId] = R.string.game_repeat
                repeatIdx = dropId
            }
            resources.getString(R.string.game_fanning) -> {
                moveWay[newdropId] = R.string.game_fanning
            }
            resources.getString(R.string.game_wake) -> {
                moveWay[newdropId] = R.string.game_wake
            }
            resources.getString(R.string.game_wash) -> {
                moveWay[newdropId] = R.string.game_wash
            }
            resources.getString(R.string.game_practice) -> {
                moveWay[newdropId] = R.string.game_practice
            }
            resources.getString(R.string.game_breakfast) -> {
                moveWay[newdropId] = R.string.game_breakfast
            }
            resources.getString(R.string.game_wave) -> {
                moveWay[newdropId] = R.string.game_wave
            }
            else -> {
                moveWay[newdropId] = -1
            }
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
                }
                isDialogShown = false
                isFirstStage = false
                isNextGame = true
                dialog.dismiss()
                initGame()
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

     override fun characterMove() {
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
    override fun backgroundVisibility(background: Int) {
        binding.ivGameBackground.loadCropImage(background)
    }

     override fun isFireCondition(): Boolean {
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

     override fun handleFireCondition() {
        // Fire 처리 로직
         blockVisibility(binding.ivGameFan, binding.ivGameFire)
    }
}

