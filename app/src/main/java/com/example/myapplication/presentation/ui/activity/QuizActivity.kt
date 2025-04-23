package com.example.myapplication.presentation.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuizBinding
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.ui.fragment.quest.CustomDialog
import com.example.myapplication.presentation.ui.fragment.quest.DialogClickListener
import com.example.myapplication.presentation.viewmodel.ChapterViewModel
import com.example.myapplication.presentation.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizActivity : BaseActivity<ActivityQuizBinding>(R.layout.activity_quiz),
    DialogClickListener {
    private lateinit var navController: NavController
    private var buttonPosition = 1
    lateinit var customDialog: CustomDialog
    val quizViewModel: QuizViewModel by viewModels()

    val titleList = listOf(
        "Q.\n무무가 가야할 방향은\n어디일까요?",
        "Q.\n알고리즘이란 무엇일까요?",
        "Q.\n사탕을 아래의 순서로 \n먹을 수 있게 도와주세요",
    )

    val subTitleList = listOf(
        "무무가 앞으로 가야 할 방향을 헷갈리고 있어요",
        "아래 4가지 선택지 중 하나를 고르세요.",
        "사탕은 위에서 부터 순서대로 먹을 수 있어요!"
    )

    private var levelCorrect = false

    override fun setLayout() {
        setNavController()
        toolBarSetting()
        setViewModel()
        nextFragment()
        moveToolBarLevel()
    }

    private fun setViewModel() {
        ViewModelProvider(this)[ChapterViewModel::class.java]
    }

    //네비게이션 컨트롤러 세팅
    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityQuizFcv.id) as NavHostFragment
        navController = navHostFragment.navController
    }

    //네비게이션마다 프레그먼트 번호로 매칭
    private fun toolBarSetting() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.quiz1Fragment -> {
                    buttonPosition = 1
                    matchNavigationWithFragment()
                }

                R.id.quiz2Fragment -> {
                    buttonPosition = 2
                    matchNavigationWithFragment()
                }

                R.id.quiz3Fragment -> {
                    buttonPosition = 3
                    matchNavigationWithFragment()
                }
                R.id.quiz4Fragment -> {
                    buttonPosition = 4
                    matchNavigationWithFragment()
                }
            }
        }
    }

    private fun matchNavigationWithFragment(){
        moveToolBarLevel()
        bindingTitle(buttonPosition - 1)
    }

    //버튼 이동
    private fun nextFragment() {
        binding.fragmentQuizBt.setOnClickListener {
            if (it.isSelected) { moveFragment() }
        }
    }

    //현재 레벨에 따른 툴 바 이동
    private fun moveToolBarLevel() {
        binding.activityQuizLevelTv.text = "$buttonPosition/3"
    }

    private fun bindingTitle(index: Int) {
        binding.itemCharacterMoveTitleTv.text = titleList[index]
        binding.itemCharacterMoveSubtitleTv.text = subTitleList[index]
    }

    fun onButtonState(isState: Boolean) {
        binding.fragmentQuizBt.isSelected = isState
    }

    @SuppressLint("InflateParams")
    private fun showCustomDialog() {
        // 다이얼로그 레이아웃을 불러옴
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_fail, null)

        // 커스텀 다이얼로그 생성
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)  // 다이얼로그 외부 터치로 종료되지 않도록

        // 다이얼로그 만들기
        val dialog = dialogBuilder.create()

        // 다이얼로그 창 설정
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)

            val sumMargins = 120

            // LayoutParams 설정
            val layoutParams = attributes
            layoutParams.width =
                (resources.displayMetrics.widthPixels - sumMargins.dpToPx()) // 양쪽 42dp 마진
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
        }

        val confirmButton = dialogView.findViewById<Button>(R.id.btn_dialog_biginner_quiz_fail)
        confirmButton.setOnClickListener {
            dialog.dismiss()
        }
        // 다이얼로그 보여주기
        dialog.show()
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun moveFragment() {
        if (levelCorrect) {
            showCustomTwoDialog()
            return
        } else {
            showCustomDialog()
        }
    }

    private fun showCustomTwoDialog() {
        when (buttonPosition) {
            1 -> setDialog("덕분에 길을 잘 찾아갔어!")
            2 -> setDialog("너무 맛있는 라면이야!")
            3 -> nextFragmentWithIndex()
        }
    }

    private fun setDialog(title: String) {
        customDialog = CustomDialog(this, title, "앞으로도 잘 부탁해~")
        customDialog.show(this.supportFragmentManager, "CustomDialog")
    }

    fun setReplaceLevelState(levelState: Boolean) {
        levelCorrect = levelState
    }

    private fun nextFragmentWithIndex() {
        when (buttonPosition) {
            1 -> { nextLevel(R.id.quiz2Fragment) }
            2 -> { nextLevel(R.id.quiz3Fragment) }
            3 -> { nextLevel(3) }
        }
    }

    private fun nextLevel(id : Int) {
        if(id == 3){
            quizViewModel.postQuizClear(1)
            Intent(this@QuizActivity, QuizClearActivity::class.java).apply {
                putExtra("game1Activity", true)
                putExtra("button state", 2)
                startActivity(this)
                finish() // QuizActivity 종료
            }
        }
        else {
            navController.navigate(
                id, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.quiz1Fragment, true)  // 시작 프래그먼트 제거
                    .setLaunchSingleTop(true)
                    .build()
            )
        }
    }

    override fun onClickNext() {
        nextFragmentWithIndex()
    }

    override fun onClickStop() {
        finish()
    }

}