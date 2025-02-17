package com.example.myapplication.presentation.ui.fragment.home

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.domain.model.ChatMessage
import com.example.myapplication.domain.model.ChatOwner
import com.example.myapplication.presentation.adapter.AdapterItemClickedListener
import com.example.myapplication.presentation.adapter.ChattingAdapter
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.HeroCactusActivity
import com.example.myapplication.presentation.ui.activity.PotionMysteryActivity
import com.example.myapplication.presentation.viewmodel.AiViewModel
import com.example.myapplication.presentation.viewmodel.CharacterViewModel
import com.example.myapplication.presentation.viewmodel.HomeViewModel
import com.example.myapplication.presentation.viewmodel.QuizViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.example.myapplication.presentation.widget.extention.loadCropImage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    AdapterItemClickedListener {

    private val homeViewModel: HomeViewModel by viewModels()
    private val characterViewModel: CharacterViewModel by viewModels()
    private val quizViewModel: QuizViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    private var chatList: MutableList<ChatMessage> = mutableListOf()
    private lateinit var chattingAdapter: ChattingAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var aiViewModel: AiViewModel
    override fun setLayout() {
        Log.d("홈 create", "df")
        initViewModel()
        initAdapter()
        initCount()
        initHome()
        initList()
        onClickBtn()
        setupBackPressedDispatcher()
        initializePersistentBottomSheet()
        initLifeCycle()
    }

    private fun initHome() {
        lifecycleScope.launch {
            homeViewModel.getDistinctHome()
        }
    }

    private fun getOnlyDate(): String {
        val today = LocalDateTime.now()
        return today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    private fun initCount() {
        quizViewModel.getCompleteQuiz()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                quizViewModel.quizComplete.collectLatest { response ->
                        Log.e("날짜", "${getOnlyDate()} ${response}")
                    if(response.payload?.isCompleted == true){
                        stateManage(3)
                    }else {
                        if (getOnlyDate() == response.payload?.date) {
                            response.payload?.solvedCount?.let { stateManage(it) }
                        }
                    }
                }
            }
        }
    }

    private fun initLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                homeViewModel.getDistinctHome.collectLatest { response ->
                    with(binding) {
                        val character = response.payload?.character
                        saveId(response)
                        when (response.result.code) {
                            200 -> {
                                Log.d("HomeFragment", "${tokenManager.getCheckBook.first()}")
                                if (character?.type == "LEVEL_HIGH" &&
                                    tokenManager.getCheckBook.first() != "check"
                                ) {
                                    // 추가된 로그
                                    characterViewModel.getRandomCactus()

                                    // Flow 명시적 수집
                                    launch {
                                        characterViewModel.getRandomCactus
                                            .collect { randomCactusResponse ->
                                                Log.d(
                                                    "HomeFragment",
                                                    "Random Cactus Response: $randomCactusResponse"
                                                )

                                                if (randomCactusResponse.result.code == 200 &&
                                                    randomCactusResponse.payload != null
                                                ) {

                                                    val name =
                                                        randomCactusResponse.payload?.cactusName
                                                    val star =
                                                        randomCactusResponse.payload?.cactusRank

                                                    val img = when (name) {
                                                        "마법사 선인장" -> R.drawable.ic_cactus_magic
                                                        "영웅 선인장" -> R.drawable.ic_cactus_hero
                                                        else -> 0
                                                    }

                                                    if (img != 0) {
                                                        startActivity(
                                                            Intent(
                                                                requireContext(),
                                                                HeroCactusActivity::class.java
                                                            ).apply {
                                                                putExtra("cactusImage", img)
                                                                putExtra("cactusName", name)
                                                                putExtra("star", star)
                                                            }
                                                        )
                                                    } else {
                                                        Log.e(
                                                            "HomeFragment",
                                                            "Invalid image for cactus"
                                                        )
                                                    }
                                                } else {
                                                    Log.e(
                                                        "HomeFragment",
                                                        "Invalid random cactus response"
                                                    )
                                                }
                                            }
                                    }
                                } else {
                                    fragmentHomeTitleTv.text =
                                        "${character!!.name}무무를 눌러서\n대화를 해보세요!"
                                    fragmentHomeCactusStateLevelTv.text =
                                        "LV.${character.level}"
                                    changeToCactusType(
                                        character.type,
                                        character.cactusType
                                    )
                                    changeToCactusName(
                                        character.type,
                                        character.cactusType
                                    )
                                    fragmentHomeCactusStateProgressPb.progress =
                                        character.activityPoints % 100
                                    fragmentHomeCactusStatePercentageTv.text =
                                        (character.activityPoints % 100).toString() + "%"
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                characterViewModel.getRandomCactus.collectLatest {
                    Log.d("getRandomCactus", "Response: $it")
                    if (it.result.code == 200 && it.payload != null) {

                        val name = it.payload?.cactusName
                        val star = it.payload?.cactusRank

                        if (name != null) { // 유효한 이미지 리소스인지 확인
                            startActivity(
                                Intent(requireContext(), HeroCactusActivity::class.java).apply {
                                    putExtra("cactusName", name)
                                    putExtra("star", star)
                                }
                            )
                        } else {
                            Log.e(
                                "getRandomCactus",
                                "Invalid image resource for cactus name: $name"
                            )
                        }
                    } else {
                        Log.e("getRandomCactus", "Invalid response or null payload: $it")
                    }
                }
            }
        }

    }

    private fun observeChatLifeCycle() {
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

    private fun saveId(response: BaseResponse<DistinctHomeIdResponse>) {
        with(tokenManager) {
            with(response) {
                lifecycleScope.launch {
                    saveUserId(payload?.id.toString())
                    saveCharacterId(payload?.character?.id.toString())
                }
            }
        }
    }

    private fun changeToCactusType(type: String, cactusType: String) {
        when (cactusType) {
            "KING_CACTUS" -> {
                setLottieFile(type)
            }

            "HERO_CACTUS", "MAGICIAN" -> {
                setImageFile(cactusType)
            }
        }
    }

    private fun changeToCactusName(type: String, cactusType: String) {
        when (cactusType) {
            "KING_CACTUS" -> {
                setKingName(type)
            }

            "HERO_CACTUS" -> {
                setSpecialName(cactusType)
            }

            "MAGICIAN" -> {
                setSpecialName(cactusType)
            }
        }
    }

    private fun setKingName(type: String?) {
        val name = when (type) {
            "LEVEL_LOW" -> "미니 선인장"
            "LEVEL_MEDIUM" -> "꽃 선인장"
            "LEVEL_HIGH" -> "킹 선인장"
            else -> ""
        }
        with(binding) {
            fragmentHomeCactusNameTv.text = name
        }
    }

    private fun setLottieFile(type: String?) {
        val raw = when (type) {
            "LEVEL_LOW" -> R.raw.mini
            "LEVEL_MEDIUM" -> R.raw.flower
            "LEVEL_HIGH" -> R.raw.king
            else -> 0
        }
        with(binding.fragmentHomeCharacterIv) {
            visibility = View.VISIBLE
            binding.fragmentHomeCharacterIv2.visibility = View.GONE
            setAnimation(raw)
            setMaxProgress(0.90f)
            playAnimation()
        }
    }

    private fun setImageFile(type: String?) {
        binding.fragmentHomeCharacterIv.visibility = View.GONE
        binding.fragmentHomeCharacterIv2.visibility = View.VISIBLE
        val img = when (type) {
            "HERO_CACTUS" -> R.drawable.ic_cactus_hero
            "MAGICIAN" -> R.drawable.ic_cactus_magic
            else -> 0
        }
        Log.d("okhttp", img.toString())
        binding.fragmentHomeCharacterIv2.loadCropImage(img)
    }

    private fun setSpecialName(type: String?) {
        val name = when (type) {
            "HERO_CACTUS" -> "영웅 선인장"
            "MAGICIAN" -> "마법사 선인장"
            else -> ""
        }
        with(binding) {
            fragmentHomeCactusNameTv.text = name
        }
    }

    private fun stateManage(count: Int) {
        when (count) {
            0 -> {
                binding.fragmentHomeTodayTakeRewordBt.visibility = View.GONE
                binding.fragmentHomeTodayMissionStamp1Iv.isSelected = false
                binding.fragmentHomeTodayMissionStamp2Iv.isSelected = false
            }

            1 -> {
                binding.fragmentHomeTodayMissionStamp1Iv.isSelected = false
                binding.fragmentHomeTodayMissionStamp2Iv.isSelected = true
            }

            2 -> {
                binding.fragmentHomeTodayMissionStamp1Iv.isSelected = true
                binding.fragmentHomeTodayMissionStamp2Iv.isSelected = true
            }

            else -> {
                binding.fragmentHomeTodayTakeRewordBt.visibility = View.VISIBLE
                binding.fragmentHomeTodayTakeRewordBt.isSelected = false
                binding.fragmentHomeTodayMissionStamp1Iv.isSelected = false
                binding.fragmentHomeTodayMissionStamp2Iv.isSelected = false
            }
        }
        buttonStateCheck()
    }

    //뷰를 보여주거나 숨겨주는 함수
    private fun visibleView(view: View, isVisible: Boolean) {
        if (isVisible) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    private fun buttonStateCheck() {
        if (binding.fragmentHomeTodayMissionStamp1Iv.isSelected && binding.fragmentHomeTodayMissionStamp2Iv.isSelected) {
            visibleView(binding.fragmentHomeTodayTakeRewordBt, true)
            binding.fragmentHomeTodayTakeRewordBt.isSelected = true
        }
    }

    private fun onClickBtn() {
        binding.fragmentHomeTodayTakeRewordBt.setOnClickListener {
            if (it.isSelected) {
                val intent = Intent(requireContext(), PotionMysteryActivity::class.java).apply {
                    putExtra("potion", 2)
                    quizViewModel.postCompleteQuiz()
                }
                startActivity(intent)
            }
        }
        binding.fragmentHomeEncyclopediaIb.setOnClickListener {
            findNavController().navigate(R.id.EncyclopediaFragment)
        }
        binding.activityMainSettingIv.setOnClickListener {
            findNavController().navigate(R.id.settingFragment)
        }
        binding.fragmentHomeNicknameBt.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.questFragment)
        }
    }

    //현재 채팅 타입 변경 (왼쪽, 오른쪽)
    private fun stateChangeChatType(chatOwner: ChatOwner) {
        CHATTYPE = chatOwner
    }

    //채팅 초기화
    private fun initChat() {
        val initList = chatList.distinct()
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
                observeChatLifeCycle()
                stateChangeChatType(ChatOwner.LEFT)
            }
        }

        binding.fragmentHomeCharacterFl.setOnClickListener {
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
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
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
                            requireActivity().onBackPressedDispatcher.onBackPressed()
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