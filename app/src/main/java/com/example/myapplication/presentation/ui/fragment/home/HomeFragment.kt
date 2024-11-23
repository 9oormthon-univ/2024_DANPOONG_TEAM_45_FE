package com.example.myapplication.presentation.ui.fragment.home

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.domain.model.home.CharacterType
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.PotionMysteryActivity
import com.example.myapplication.presentation.viewmodel.HomeViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.example.myapplication.presentation.widget.extention.loadCropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by activityViewModels()

    

    @Inject
    lateinit var tokenManager: TokenManager

    override fun setLayout() {
        initCount()
        initHome()
        observeLifeCycle()
        onClickBtn()
    }

    override fun onResume() {
        super.onResume()
        initCount()
        initHome()
    }

    private fun initHome() {
        lifecycleScope.launch {
            try {
                stateManage(tokenManager.getCountToken.first().toString().toInt())
                Log.d("결과", tokenManager.getCountToken.first().toString())
            }catch (e : Exception){
                stateManage(0)
            }
        }
        homeViewModel.getDistinctHome()
    }

    private fun initCount() {
        lifecycleScope.launch {
            val today = getOnlyDate()
            val prepareDay = tokenManager.getDateToken.first().toString()
            if (today != prepareDay) {
                Log.d("결과","$today ,${prepareDay}")
                tokenManager.saveCountToken("0")
                tokenManager.saveDateToken(today)
            }
        }
    }
    private fun getOnlyDate(): String {
        val today = LocalDateTime.now()
        return today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                homeViewModel.getDistinctHome.collectLatest {
                    with(binding) {
                        val character = it.payload?.character
                        saveId(it)
                        when (it.result.code) {
                            200 -> {
                                fragmentHomeTitleTv.text = "반가워요\n저는 ${character!!.name}무무예요!"
                                fragmentHomeCactusStateLevelTv.text =
                                    "LV.${character.level}"
                                fragmentHomeCharacterIv.loadCropImage(
                                    setCharacterDrawable(stringToEnum(character.type))
                                )
                                Log.d(
                                    "이미지",
                                    "${
                                        resources.getResourceEntryName(
                                            setCharacterDrawable(stringToEnum(character.type))
                                        )
                                    }"
                                )
                                fragmentHomeCactusStateProgressPb.progress = character.activityPoints%100
                                fragmentHomeCactusStatePercentageTv.text = (character.activityPoints%100).toString() + "%"
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveId(response: BaseResponse<DistinctHomeIdResponse>) {
        with(tokenManager) {
            with(response) {
                lifecycleScope.launch {
                    saveUserId(payload?.id.toString())
                    saveCharacterId(payload?.id.toString())
                }
            }
        }
    }

    private fun stringToEnum(value: String): CharacterType {
        return when (value) { // 서버에서 온 값을 소문자로 변환
            "LEVEL_LOW" -> CharacterType.LEVEL_LOW
            "LEVEL_MEDIUM" -> CharacterType.LEVEL_MEDIUM
            "LEVEL_HIGH" -> CharacterType.LEVEL_HIGH
            else -> CharacterType.LEVEL_LOW // 매핑되지 않는 값 처리
        }
    }

    private fun setCharacterDrawable(characterType: CharacterType): Int {
        return when (characterType) {
            CharacterType.LEVEL_LOW -> R.drawable.ic_cactus_1
            CharacterType.LEVEL_MEDIUM -> R.drawable.ic_cactus_2
            CharacterType.LEVEL_HIGH -> R.drawable.ic_cactus_3
        }
    }

    private fun stateManage(count: Int) {
        when (count) {
            0-> {
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
                    putExtra("potion",2)
                    lifecycleScope.launch {
                        tokenManager.saveCountToken("3")
                    }
                }
                startActivity(intent)
            }
        }
        binding.activityMainSettingIv.setOnClickListener {
            findNavController().navigate(R.id.settingFragment)
        }
        binding.fragmentHomeNicknameBt.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.questFragment)
        }
    }


}