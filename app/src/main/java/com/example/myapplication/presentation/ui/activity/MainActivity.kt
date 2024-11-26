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
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    AdapterItemClickedListener {


    @Inject
    lateinit var tokenManager: TokenManager

    private var chatList: MutableList<ChatMessage> = mutableListOf()
    private lateinit var chattingAdapter: ChattingAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var aiViewModel: AiViewModel
    private lateinit var navController: NavController

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

    //현재 채팅 타입 변경 (왼쪽, 오른쪽)
    private fun stateChangeChatType(chatOwner: ChatOwner) {
        CHATTYPE = chatOwner
    }

    //채팅 초기화
    private fun initChat() {
        val initList = chatList
        chattingAdapter.submitList(initList)
        stateChangeChatType(ChatOwner.LEFT)
    }

    //리스트 초기화
    private fun initList() {
        val initList = chatList
        chattingAdapter.submitList(initList)
    }

    //어댑터 초기화
    private fun initAdapter() {
        chattingAdapter = ChattingAdapter(this)
        chatList.clear()
        chattingAdapter.submitList(chatList)
        binding.recyclerView.adapter = chattingAdapter
    }

    //뷰모델 초기화
    private fun initViewModel() {
        aiViewModel = ViewModelProvider(this)[AiViewModel::class.java]
    }

    //바텀 네비게이션 세팅
    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.activityMainFcv.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.activityMainBnv.setupWithNavController(navController)
    }

    //채팅 도착 시 값 수령
    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                aiViewModel.ai.collectLatest {
                    if (it.result.code == 200) {
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
    }

    //바텀 시트 (챗 봇) 메세지 입력 및 초기화 함수
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

    //뒤로가기 클릭 시 바텀 네비 사라짐
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

    //클릭 시 서버로 채팅 전달
    override fun onClick(item: Any) {
        item as ChatMessage.LEFT
        stateChangeChatType(ChatOwner.LEFT)
        aiViewModel.postAi(item.message)
    }

    //채팅 타입 오른쪽으로 초기화
    companion object {
        private var CHATTYPE = ChatOwner.RIGHT
    }

}