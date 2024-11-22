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
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun setLayout() {
        observeLifeCycle()
        stateManage()
        onClickBtn()
    }

    override fun onStart() {
        super.onStart()
        initHome()
    }

    private fun initHome() {
        homeViewModel.getDistinctHome()
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
                                fragmentHomeTitleTv.text = "반가워요\n저는 ${character!!.name}무무에요!"
                                fragmentHomeCactusStateLevelTv.text =
                                    "LV.${character.level}"
                                fragmentHomeCharacterIv.loadCropImage(
                                    setCharacterDrawable(stringToEnum(character.type))
                                )
                                Log.d("이미지","${resources.getResourceEntryName(setCharacterDrawable(stringToEnum(character.type)))}")
                                fragmentHomeCactusStateProgressPb.progress =
                                    character.activityPoints
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
        return when (value.lowercase()) { // 서버에서 온 값을 소문자로 변환
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

    private fun stateManage() {
        binding.fragmentHomeTodayMissionStamp1Iv.setOnClickListener {
            it.isSelected = true
            buttonStateCheck()
        }
        binding.fragmentHomeTodayMissionStamp2Iv.setOnClickListener {
            it.isSelected = true
            buttonStateCheck()
        }
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
                startActivity(Intent(requireContext(), PotionMysteryActivity::class.java))
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