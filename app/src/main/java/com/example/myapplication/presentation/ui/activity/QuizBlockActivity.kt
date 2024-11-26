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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.DRAG_FLAG_GLOBAL
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
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
    private val dragSources = mutableListOf<View>()
    private var moveWay = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var targetBlockMap = mutableMapOf<Int, Int?>()

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
            val overlayTextView = TextView(this).apply {
                text = draggedTextView?.text
                textSize = 12f
                setTextColor(ContextCompat.getColor(this@QuizBlockActivity, R.color.white))
                setPadding(20, 25, 0, 0)
            }
            targetView.addView(overlayTextView, 1)  // 새 텍스트 뷰를 두 번째 위치에 추가
        } else {
            Log.e("handleImageDrop", "Target is not a FrameLayout")
        }

        // 블록 이동 처리
        handleBlockMove(blockMove!!, dropId)
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