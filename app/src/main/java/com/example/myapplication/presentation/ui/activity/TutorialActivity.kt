package com.example.myapplication.presentation.ui.activity

import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityTutorialBinding
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.ui.fragment.quest.DialogClickListener

class TutorialActivity: BaseActivity<ActivityTutorialBinding>(R.layout.activity_tutorial),
    DialogClickListener {

    private lateinit var navController: NavController
    private var buttonPosition = 1
    private var levelCorrect = false
    private var basicBlockId = 1 // 생성되는 블록 아이디 - 블록 색 지정을 위해 만든 변수

    override fun setLayout() {
        setNavController()
        initBlock()
        buttonNumSetting()
        nextFragment()
    }

    //네비게이션 컨트롤러 세팅
    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityTutorialFcv.id) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun buttonNumSetting() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.tutorialFragment1 -> {
                    buttonPosition = 1
                }

                R.id.tutorialFragment2 -> {
                    buttonPosition = 2
                }

                R.id.tutorialFragment3 -> {
                    buttonPosition = 3
                }

                R.id.tutorialFragment4 -> {
                    buttonPosition = 4
                }

                R.id.tutorialFragment5 -> {
                    buttonPosition = 5
                }
            }
        }
    }

    private fun nextFragmentWithIndex() {
        when (buttonPosition) {
            1 -> {
                navController.navigate(
                    R.id.tutorialFragment2, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.tutorialFragment1, true)  // 시작 프래그먼트 제거
                        .setLaunchSingleTop(true)
                        .build()
                )
            }

            2 -> {
                navController.navigate(
                    R.id.tutorialFragment3, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.tutorialFragment2, true)  // 이전 프래그먼트 제거
                        .setLaunchSingleTop(true)
                        .build()
                )
            }

            3 -> {
                navController.navigate(
                    R.id.tutorialFragment4, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.tutorialFragment3, true)  // 이전 프래그먼트 제거
                        .setLaunchSingleTop(true)
                        .build()
                )
            }

            4 -> {
                navController.navigate(
                    R.id.tutorialFragment5, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.tutorialFragment4, true)  // 이전 프래그먼트 제거
                        .setLaunchSingleTop(true)
                        .build()
                )
            }

            5 -> {
                val intent = Intent(this, QuizBlockActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //버튼 이동
    private fun nextFragment() {
        binding.activityTutorialConstraint.setOnClickListener {
            nextFragmentWithIndex()
        }
    }

    fun setReplaceLevelState(levelState: Boolean) {
        levelCorrect = levelState
    }

    override fun onClickNext() {
        nextFragmentWithIndex()
    }

    override fun onClickStop() {
        finish()
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
                container?.addView(newBlock)
                newBlock.tag = block  // BlockDTO를 tag로 설정
            }
        }
    }
}