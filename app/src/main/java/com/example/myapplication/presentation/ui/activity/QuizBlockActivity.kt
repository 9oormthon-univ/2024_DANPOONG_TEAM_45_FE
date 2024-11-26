package com.example.myapplication.presentation.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.DRAG_FLAG_GLOBAL
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.DragStartHelper
import androidx.draganddrop.DropHelper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityGameBinding
import com.example.myapplication.databinding.ActivityQuizBinding
import com.example.myapplication.databinding.ActivityQuizBlockBinding
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.ui.fragment.quest.CustomDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QuizBlockActivity : BaseActivity<ActivityQuizBlockBinding>(R.layout.activity_quiz_block) {

    private lateinit var navController: NavController
    private var buttonPosition = 1
    lateinit var customDialog: CustomDialog
    private var draggedTextView: TextView? = null
    private val dragSources = mutableListOf<FrameLayout>()
    private var moveWay = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var targetBlockMap = mutableMapOf<Int, Int?>()
    private var isRepeat = false
    private var repeatIdx: Int = -1

    val messageList = listOf(
        "무무가 아침 일정을 잘 마치도록 도와줘\n일어나기 > 세수하기 > 아침먹기 > 준비하기\n순서로 부탁할게! 해줄 수 있지?",
        "바다에서 파도 소리를 듣고 싶어! 파도는\n일정한 주기로 밀려왔다가 사라진대 파도소리가\n3번 들리게 만들어줄 수 있어?"
    )

    private var levelCorrect = true

    private val dropTargets by lazy {
        mutableListOf<View>(
            binding.ibBiginnerGame1Space1,
            binding.ibBiginnerGame1Space2,
            binding.ibBiginnerGame1Space3,
            binding.ibBiginnerGame1Space4
        )
    }

    override fun setLayout() {
        setNavController()
        storySetting()
        nextFragment()
        onStoryState(true)
        setupDropTargets(dropTargets, this)
    }

    fun handleViewFromFragment(view: FrameLayout) {
        // 전달받은 view를 처리하는 로직
        dragSources.add(view)
    }

    //네비게이션 컨트롤러 세팅
    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityQuizBlockFcv.id) as NavHostFragment
        navController = navHostFragment.navController
    }

    //네비게이션마다 프레그먼트 번호로 매칭
    private fun storySetting() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.quizBlock1Fragment -> {
                    buttonPosition = 1
                    binding.ivQuizBlockBackground.setImageResource(R.drawable.iv_biginner_game1_image)
                    binding.linearLayoutBlockList
                    bindingStory(0)
                }

                R.id.quizBlock2Fragment -> {
                    buttonPosition = 2
                    binding.ivQuizBlockBackground.setImageResource(R.drawable.iv_biginner_game2_image)
                    bindingStory(1)
                }
            }
        }

        binding.ibGameplayExitBtn.setOnClickListener {
        }

    }

    fun clearDragTargets() {
        dragSources.clear()
        binding.linearLayoutBlockList.removeAllViews()
    }

    fun clearDropTargets() {
        targetBlockMap = mutableMapOf() // targetBlockMap 초기화

        // 드래그된 소스들을 다시 보이게 설정
        for (dragSource in dragSources) {
            dragSource.visibility = View.VISIBLE
        }

        // 각 Drop Target에 대해 초기화 작업 수행
        dropTargets.forEach { target ->
            // 각 타겟 뷰에 대해 작업 처리
            if (target is FrameLayout) {
                // 1. Tag를 안전하게 가져옴
                val removeTarget1 = target.getTag(R.id.ib_biginner_game1_space1) as? ImageView
                val removeTarget2 = target.getTag(R.id.ib_biginner_game1_space2) as? ImageView
                val removeTarget3 = target.getTag(R.id.ib_gamestop_btn) as? ImageView
                val removeTarget4 = target.getTag(R.id.ib_gameplay_btn) as? EditText
                val removeTarget5 = target.getTag(R.id.ib_game_state_done) as? TextView

                // 2. 태그가 설정된 뷰들을 제거
                removeTarget1?.visibility = View.GONE
                removeTarget2?.visibility = View.GONE
                removeTarget3?.visibility = View.GONE
                removeTarget4?.visibility = View.GONE
                removeTarget5?.visibility = View.GONE

                // 3. 첫 번째 자식 뷰(ImageView) 리셋
                val targetImageView = target.getChildAt(0) as? ImageView
                targetImageView?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.shape_square_rounded_16dp))

                // 4. TextView 처리
                if (target.childCount > 1) {
                    target.removeViewAt(1) // 기존 텍스트 뷰 제거
                }

                // 5. 새 텍스트 뷰 추가
                val overlayTextView = TextView(this).apply {
                    text = "" // 텍스트 초기화
                    setTextColor(ContextCompat.getColor(this@QuizBlockActivity, R.color.white))
                    setPadding(20, 25, 0, 0)
                }
                target.addView(overlayTextView, 1) // 두 번째 자리에 텍스트 뷰 추가
            } else {
                Log.e("clearDropTargets", "Target is not a FrameLayout")
            }
        }
    }

    //버튼 이동
    private fun nextFragment() {
        binding.ibGameplayBtn.setOnClickListener {
            onGameplayState()
            moveFragment()
        }
    }

    private fun bindingStory(index: Int) {
        binding.ibGamestoryMsgTxt.text = messageList[index]
    }

    fun onGameplayState() {
        binding.ibGameplayBtn.visibility = View.GONE
        binding.ibGamestopBtn.visibility = View.VISIBLE
    }

    fun onGamestopState() {
        binding.ibGameplayBtn.visibility = View.VISIBLE
        binding.ibGamestopBtn.visibility = View.GONE
    }

    fun onStoryState(isState: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.ibGamestoryMsgTxt.isSelected = !isState
            binding.ibGamestoryMsg.isSelected = !isState
        }, 10000)  // 10초 후 메시지 사라짐
    }

    fun setupDragSources(dragSources: List<View>) {
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

    // drop의 target id 찾기
    fun setupDropTargets(dropTargets: List<View>, context: Context) {
        val activity = QuizBlockActivity()

        dropTargets.forEach { target ->
            DropHelper.configureView(
                activity,
                target,
                arrayOf(MIMETYPE_TEXT_PLAIN),
                DropHelper.Options.Builder()
                    .setHighlightColor(ContextCompat.getColor(context, R.color.water_color))
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

    fun handleImageDrop(target: View, dragId: Int, dropId: Int) {
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
                            setTextColor(ContextCompat.getColor(this@QuizBlockActivity, R.color.white))
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
                            setTextColor(ContextCompat.getColor(this@QuizBlockActivity, R.color.white))
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
                        setTextColor(ContextCompat.getColor(this@QuizBlockActivity, R.color.white))
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
        var newdropId: Int
        if (repeatIdx == dropId) {
            newdropId = dropId + 1
        } else {
            newdropId = dropId
        }
        // val draggedTextView = draggedBlock.getChildAt(1) as TextView
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

    @SuppressLint("InflateParams")
    private fun showCustomDialog() {
        // 다이얼로그 레이아웃을 불러옴
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_fail, null)

        // 커스텀 다이얼로그 생성
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)  // 다이얼로그 외부 터치로 종료되지 않도록

        // 다이얼로그 만들기
        val dialog = dialogBuilder.create()

        val confirmButton = dialogView.findViewById<Button>(R.id.btn_dialog_biginner_quiz_fail)
        confirmButton.setOnClickListener {
            dialog.dismiss()
        }
        // 다이얼로그 보여주기
        dialog.show()
    }

    private fun setDialog() {
        onGamestopState()
        when (buttonPosition) {
            1 -> showCustomTwoDialog(1)
            2 -> showCustomTwoDialog(2)
        }
    }

    private fun moveFragment() {
        if (levelCorrect) {
            setDialog()
        } else {
            showCustomDialog()
        }
    }

    private fun showCustomTwoDialog(id: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_success, null)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)

        val dialog = dialogBuilder.create()

        val title = dialogView.findViewById<TextView>(R.id.dialog_button_two_title)
        val subTitle = dialogView.findViewById<TextView>(R.id.dialog_button_two_subtitle)
        val stopBtn = dialogView.findViewById<Button>(R.id.dialog_button_stop)
        val nextBtn = dialogView.findViewById<Button>(R.id.dialog_button_next_step)

        if (id == 1) {
            title.text = "완벽한 아침이야!"
            subTitle.text = "상쾌한 아침을 만들어줘서 고마워 :)"
        } else {
            title.text = "우와, 멋지다!"
            subTitle.text = "파도 소리를 듣게 해줘서 고마워 :)"
        }

        stopBtn.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        nextBtn.setOnClickListener {
            dialog.dismiss()
            nextFragmentWithIndex()
        }

        dialog.show() // 다이얼로그 표시
    }

    fun setReplaceLevelState(levelState: Boolean) {
        levelCorrect = levelState
    }

    private fun nextFragmentWithIndex() {
        clearDragTargets()
        clearDropTargets()
        navController.navigate(
            R.id.quizBlock2Fragment, null,
            NavOptions.Builder()
                .setPopUpTo(R.id.quizBlock1Fragment, true)  // 시작 프래그먼트 제거
                .setLaunchSingleTop(true)
                .build()
        )
    }

    fun onClickNext() {
        nextFragmentWithIndex()
    }

    fun onClickStop() {
        finish()
    }
}