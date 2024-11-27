package com.example.myapplication.presentation.ui.fragment.quest

import android.media.MediaPlayer
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityGameBinding
import com.example.myapplication.databinding.FragmentQuizBlock2Binding
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.BlockDTO
import com.example.myapplication.presentation.ui.activity.GameInterface
import com.example.myapplication.presentation.ui.activity.QuizBlockActivity
import com.example.myapplication.presentation.viewmodel.QuizViewModel

class QuizBlock2Fragment : BaseFragment<FragmentQuizBlock2Binding>(R.layout.fragment_quiz_block_2), GameInterface {

    private var targetBlockMap = mutableMapOf<Int, Int?>()
    private var isExit = false //나가기 버튼 클릭했는지 여부 판단
    private var isDialogShown = false // 다이얼로그 표시 상태 플래그
    private var isFailDialogShown = false
    private lateinit var mediaPlayer: MediaPlayer

    private val dragSources = mutableListOf<View>()
    private var basicBlockId = 1 // 생성되는 블록 아이디 - 블록 색 지정을 위해 만든 변수
    private var repeatBlockId = 1 // 생성되는 블록 아이디
    private var curGameId = 2
    private var chapterId = 1

    private var isState = false
    private var moveWay = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    private var isRepeat = false
    private var repeatIdx: Int = -1
    private var draggedTextView: TextView? = null
    private lateinit var viewModel: QuizViewModel

    override fun setLayout() {
        // 블록 튜토리얼 2번
        initViewModel()
        initGame()
        initBlock()
        initMediaPlayer()
        setupDragSources(dragSources)
        updateViewModel()
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]
    }

    private fun updateViewModel() {
        viewModel.moveWay.observe(viewLifecycleOwner, Observer { moveWay ->
            // moveWay 값 변경 시 처리할 로직
            this.moveWay = moveWay
            Log.d("moveWay", moveWay.toString())
        })
    }

    override fun initBlock() {
        val blocks = listOf(
            BlockDTO(resources.getString(R.string.block_type_normal), "파도 소리 재생", 0),
            BlockDTO(resources.getString(R.string.block_type_repeat), "번 반복하기", 3),
        )
        blocks.forEach { block -> addBlock(block) }
    }

    override fun initGame() {
        val av = requireActivity() as QuizBlockActivity
        isExit = false
        isDialogShown = false

        targetBlockMap = mutableMapOf()
        for (dragSource in dragSources) {
            dragSource.visibility = View.VISIBLE
        }
        isFailDialogShown = false
    }

    override fun addBlock(block: BlockDTO) {
        val container = requireActivity().findViewById<LinearLayout>(R.id.linearLayout_block_list)

        when (block.blockType) {
            resources.getString(R.string.block_type_normal) -> {
                // FrameLayout 생성
                val newBlock = FrameLayout(requireActivity()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    id = basicBlockId
                    basicBlockId += 1
                }

                // ImageView 추가
                val imageView = ImageView(requireActivity()).apply {
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
                val textView = TextView(requireActivity()).apply {
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

                dragSources.add(newBlock)
                (activity as? QuizBlockActivity)?.handleViewFromFragment(newBlock)
                newBlock.tag = block  // BlockDTO를 tag로 설정
            }
            resources.getString(R.string.block_type_repeat) -> {
                val newBlock = FrameLayout(requireActivity()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }

                val imageView1 = ImageView(requireActivity()).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                        setMargins(0, 3.dpToPx(), 0, 0)
                    }
                    setImageResource(R.drawable.ib_gameplay2_repeat)
                }
                val imageView2 = ImageView(requireActivity()).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                        setMargins(0, 8.dpToPx(), 32.dpToPx(), 0) // 마진 설정
                    }
                    setImageResource(R.drawable.iv_repeat_cnt)
                }

                val editText = EditText(requireActivity()).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                        setMargins(0, 0, 29.dpToPx(), 0) // 마진 설정
                    }
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    setText(block.repeat.toString())
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    textSize = 10.51f
                }

                val imageView3 = ImageView(requireActivity()).apply {
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
                container?.addView(newBlock)
                dragSources.add(newBlock)
                (activity as? QuizBlockActivity)?.handleViewFromFragment(newBlock)
                newBlock.tag = block  // BlockDTO를 tag로 설정
            }
        }
    }

    override fun handleImageDrop(target: View, dragId: Int, dropId: Int) {
        TODO("Not yet implemented")
    }

    override fun gameFunction(binding: ActivityGameBinding) {
        TODO("Not yet implemented")
    }

    override fun checkSuccess() {
        TODO("Not yet implemented")
    }
    override fun showSuccessDialog(exit: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showFailDialog() {
        TODO("Not yet implemented")
    }

    override fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }

    private fun initMediaPlayer(){
        // MediaPlayer 초기화
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.tide)
        mediaPlayer.isLooping = true // 반복 재생
        mediaPlayer.start() // 음악 시작
    }
    override fun onPause() {
        super.onPause()
        // Activity가 중단되면 음악 일시 중지
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        // Activity가 다시 활성화되면 음악 재개
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // MediaPlayer 해제
        mediaPlayer.release()
    }
}