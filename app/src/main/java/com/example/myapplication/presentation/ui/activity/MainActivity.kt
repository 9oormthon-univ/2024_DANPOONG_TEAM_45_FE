package com.example.myapplication.presentation.ui.activity

import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.domain.model.ChatMessage
import com.example.myapplication.domain.model.ChatOwner
import com.example.myapplication.presentation.adapter.AdapterItemClickedListener
import com.example.myapplication.presentation.adapter.ChattingAdapter
import com.example.myapplication.presentation.base.BaseActivity
import com.example.myapplication.presentation.viewmodel.AiViewModel
import com.example.myapplication.presentation.viewmodel.ChapterViewModel
import com.example.myapplication.presentation.viewmodel.CharacterViewModel
import com.example.myapplication.presentation.viewmodel.DifficultyViewModel
import com.example.myapplication.presentation.viewmodel.EduViewModel
import com.example.myapplication.presentation.viewmodel.HomeViewModel
import com.example.myapplication.presentation.viewmodel.QuizViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    AdapterItemClickedListener {
    private var chatList: MutableList<ChatMessage> = mutableListOf()
    private lateinit var chattingAdapter: ChattingAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var aiViewModel: AiViewModel

    private lateinit var navController: NavController
    private lateinit var eduViewModel: EduViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var chapterViewModel: ChapterViewModel
    private lateinit var quizVieModel: QuizViewModel
    private lateinit var difficultyViewModel: DifficultyViewModel

    override fun setLayout() {
        initViewModel()
        initAdapter() // chattingAdapter 초기화
        initList()    // 초기화 이후 호출
        setBottomNavigation()
        setupBackPressedDispatcher()
        observeLifeCycle()
        initializePersistentBottomSheet()
    }

    fun hideFloating() {
        binding.fragmentNewsDescriptionFtb.visibility = View.GONE
    }

    fun showFloating() {
        binding.fragmentNewsDescriptionFtb.visibility = View.VISIBLE
    }

    var CHATTYPE = ChatOwner.RIGHT


    fun stateChangeChatType(chatOwner: ChatOwner) {
        CHATTYPE = chatOwner
    }

    private fun initChat() {
        val initList = chatList
        chattingAdapter.submitList(initList)
        stateChangeChatType(ChatOwner.LEFT)

    }

    private fun initList() {
        val initList = chatList
        chattingAdapter.submitList(initList)
    }

    fun initAdapter() {
        chattingAdapter = ChattingAdapter(this)
        chatList.clear()
        chattingAdapter.submitList(chatList)
        binding.recyclerView.adapter = chattingAdapter
    }

    @Inject
    lateinit var tokenManager: TokenManager
    private fun initViewModel() {
        eduViewModel = ViewModelProvider(this)[EduViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        characterViewModel = ViewModelProvider(this)[CharacterViewModel::class.java]
        chapterViewModel = ViewModelProvider(this)[ChapterViewModel::class.java]
        quizVieModel = ViewModelProvider(this)[QuizViewModel::class.java]
        difficultyViewModel = ViewModelProvider(this)[DifficultyViewModel::class.java]
        aiViewModel = ViewModelProvider(this)[AiViewModel::class.java]
    }

    //바텀 네비게이션 세팅
    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityMainFcv.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.activityMainBnv.setupWithNavController(navController)
    }

    private fun observeLifeCycle() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                aiViewModel.ai.collectLatest {
                    if (it.result.code == 200) {
                        Log.d("페이", "${it.payload.toString()}")
                        when (CHATTYPE) {
                            ChatOwner.RIGHT -> {
                                chatList.add(ChatMessage.RIGHT(it.payload.toString()))
                            }

                            ChatOwner.LEFT -> {
                                chatList.add(ChatMessage.LEFT(it.payload.toString()))
                            }

                        }
                    }
                    initChat()
                    binding.recyclerView.adapter = chattingAdapter
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                eduViewModel.getAllQuiz.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                eduViewModel.getDistinctQuiz.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                homeViewModel.getAllHome.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                characterViewModel.postCharacter.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                chapterViewModel.getDistinctChapter.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                chapterViewModel.getAllChapter.collectLatest {
                    Log.d("값 도착", it.toString())
                }
            }
        }
    }

    private fun initializePersistentBottomSheet() {
        binding.layoutChatBottomSheetSendBt.setOnClickListener {
            val message = binding.layoutChatBottomSheetSendEt.text.toString()
            if (message.isNotBlank()) {
                val newChatList = chatList.toMutableList()  // 새로운 리스트 생성
                newChatList.add(ChatMessage.RIGHT(message))  // 새로운 메시지를 추가
                chattingAdapter.submitList(newChatList) {
                    binding.recyclerView.scrollToPosition(-1)
                }  // 새로운 리스트를 전달
                chatList = newChatList  // 기존 리스트를 새로운 리스트로 교체
                binding.layoutChatBottomSheetSendEt.text.clear()  // 입력 필드를 초기화
                aiViewModel.postAi(message)
                stateChangeChatType(ChatOwner.LEFT)
            }
        }

        binding.fragmentNewsDescriptionFtb.setOnClickListener {
            // BottomSheet의 peek_height만큼 보여주기
            binding.mainEt.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.mainEt.visibility = View.GONE
                    BottomSheetBehavior.STATE_EXPANDED -> binding.mainEt.visibility = View.VISIBLE
                    BottomSheetBehavior.STATE_COLLAPSED -> binding.mainEt.visibility = View.GONE
                    BottomSheetBehavior.STATE_DRAGGING -> Log.d("MainActivity", "state: dragging")
                    BottomSheetBehavior.STATE_SETTLING -> Log.d("MainActivity", "state: settling")
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> binding.mainEt.visibility =
                        View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 사용자가 드래그한 위치를 저장
                bottomSheetBehavior.saveFlags = BottomSheetBehavior.SAVE_ALL
            }
        })

        // 사용자가 드래그한 위치까지만 열리도록 설정
        bottomSheetBehavior.isFitToContents = false
        bottomSheetBehavior.halfExpandedRatio = 0.5f // 기본값, 필요에 따라 조정
    }

    private fun setupBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (bottomSheetBehavior.state) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        binding.mainEt.visibility = View.GONE
                    }

                    else -> {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        })
    }


    override fun onClick(item: Any) {
        item as ChatMessage.LEFT
        stateChangeChatType(ChatOwner.LEFT)
        aiViewModel.postAi(item.message)
    }

}