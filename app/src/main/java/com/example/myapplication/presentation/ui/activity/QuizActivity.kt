package com.example.myapplication.presentation.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.ui.fragment.quest.CustomDialog
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQuizBinding
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
    val chapterViewModel : ChapterViewModel by viewModels()
    val quizViewModel : QuizViewModel by viewModels()

    val titleList = listOf(
        "Q.\n무무가 가야할 방향은\n어디일까요?",
        "Q.\n맛있는 라면은 어떤 순서로 \n요리 해야할까요?",
        "Q.\n알고리즘이란 무엇일까요?"
    )

    val subTitleList = listOf(
        "무무가 앞으로 가야 할 방향을 헷갈리고 있어요",
        "무무가 순서를 잘 기억할 수 있도록 도와주세요!",
        "아래 4가지 선택지 중 하나를 고르세요."
    )

    private var levelCorrect = false

    override fun setLayout() {
        setNavController()
        toolBarSetting()
        setViewModel()
        nextFragment()
        moveToolBarLevel()
        observeLifeCycle()
    }

    private fun setViewModel(){
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
                    moveToolBarLevel()
                    bindingTitle(buttonPosition - 1)
                }

                R.id.quiz2Fragment -> {
                    buttonPosition = 2
                    moveToolBarLevel()
                    bindingTitle(buttonPosition - 1)
                }

                R.id.quiz3Fragment -> {
                    buttonPosition = 3
                    moveToolBarLevel()
                    bindingTitle(buttonPosition - 1)
                }
            }
        }
    }

    //버튼 이동
    private fun nextFragment() {
        binding.fragmentQuizBt.setOnClickListener {
            if (it.isSelected) {
                moveFragment()
            }
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

    private fun moveFragment() {
        if (levelCorrect) {
            showCustomTwoDialog()
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
            1 -> {
                navController.navigate(
                    R.id.quiz2Fragment, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.quiz1Fragment, true)  // 시작 프래그먼트 제거
                        .setLaunchSingleTop(true)
                        .build()
                )
            }

            2 -> {
                navController.navigate(
                    R.id.quiz3Fragment, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.quiz2Fragment, true)  // 이전 프래그먼트 제거
                        .setLaunchSingleTop(true)
                        .build()
                )
            }

            3 -> {
                quizViewModel.postQuizClear(1)
            }
        }
    }

    private fun observeLifeCycle(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                quizViewModel.quizClear.collectLatest {
                    when(it.result.code){
                        200 -> {
                            intent.putExtra("game1Activity", true)
                            intent.putExtra("button state", 2)
                            startActivity(Intent(this@QuizActivity, QuizClearActivity::class.java))
                            finish() // QuizActivity 종료
                        }
                        
                    }
                }
            }
        }
    }

    override fun onClickNext() {
        nextFragmentWithIndex()
    }

    override fun onClickStop() {
        finish()
    }

}