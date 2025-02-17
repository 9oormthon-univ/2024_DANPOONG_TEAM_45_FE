package com.example.myapplication.presentation.ui.fragment.account

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.data.base.BaseLoadingState
import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.presentation.adapter.LoginBannerViewPagerAdapter
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.MainActivity
import com.example.myapplication.presentation.viewmodel.HomeViewModel
import com.example.myapplication.presentation.viewmodel.LoginViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    @Inject
    lateinit var tokenManager: TokenManager
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var onBoardingViewPagerAdapter: LoginBannerViewPagerAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    //화면 진입 시 토큰 저장소 초기화
    override fun onStart() {
        super.onStart()
        initRemainToken()
    }

    override fun setLayout() {
        initPager()
        initViewModel()
        setOnclickBtn()
    }

    private fun showProgress() {
        binding.lottieProgressBar.visibility = View.VISIBLE
        binding.lottieProgressBar.playAnimation()
    }

    private fun hideProgress() {
        binding.lottieProgressBar.cancelAnimation()
        binding.lottieProgressBar.visibility = View.GONE
    }

    //뷰 페이저 + 인디케이터 배너 아이템 세팅
    private fun initPager() {
        val list = arrayListOf(
            R.drawable.ic_onboarding_image_1,
            R.drawable.ic_onboarding_image_2
        )
        onBoardingViewPagerAdapter = LoginBannerViewPagerAdapter(list)

        with(binding.activityAccountViewVp) {
            adapter = onBoardingViewPagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL

            // 시작 위치를 중간으로 설정
            val startPosition = Int.MAX_VALUE / 2
            setCurrentItem(startPosition - (startPosition % list.size), false)

            binding.activityAccountIndicatorIc.apply {
                createIndicators(list.size, 0)  // 실제 이미지 개수만큼만 인디케이터 생성
            }

            // ViewPager2의 페이지 변경 리스너
            binding.activityAccountViewVp.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // 실제 포지션으로 인디케이터 업데이트
                    val realPosition = position % list.size
                    binding.activityAccountIndicatorIc.animatePageSelected(realPosition)
                }
            })
        }
    }

    //잔여 토큰 초기화
    private fun initRemainToken() {
        runBlocking {
            tokenManager.deleteAccessToken()
        }
    }

    //뷰 모델 초기화
    private fun initViewModel() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    //카카오 로그인 버튼
    private fun setOnclickBtn() {
        binding.activityAccountKakaoLoginBt.setOnClickListener {
            userKakaoLogin()
            showProgress()
        }
    }

    //엑세스 토큰 전송
    private fun sendKakaoAccessToken(kakaoAccessToken: String) = LogInKakaoDto(kakaoAccessToken)

    //카카오 유저 로그인
    private fun userKakaoLogin() {
        UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
            if (error != null) {
                Log.e("카카오", "로그인 실패", error)
                Toast.makeText(requireContext(), "로그인 실패", Toast.LENGTH_SHORT).show()
            } else if (token != null) {
                observeLifeCycle()
                loginViewModel.postKakaoLogin(sendKakaoAccessToken(token.accessToken))
                Log.i("카카오", "로그인 성공 ${token.accessToken}")
            }
        }
    }

    //토큰 수령 시 다음 화면으로 넘어감
    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    loginViewModel.kakaoLogin.collectLatest {
                        when (it.state) {
                            BaseLoadingState.IDLE -> {}
                            BaseLoadingState.LOADING -> {}
                            BaseLoadingState.SUCCESS -> {
                                with(it.payload) { saveToken(accessToken, refreshToken, picture, nickname) }
                            }
                            BaseLoadingState.ERROR -> {}
                        }
                    }
                }//launch
                launch {
                    homeViewModel.getDistinctHome.collectLatest {
                        val result = it
                        Log.d("결과 정보", "${it.result}")
                        when (result.status) {
                            BaseLoadingState.IDLE -> {}
                            BaseLoadingState.LOADING -> {}
                            BaseLoadingState.SUCCESS -> { callResultCode(it.result.code) }
                            BaseLoadingState.ERROR -> {}
                        }
                        hideProgress()
                    }
                }//launch
            }//repeatOnLifeCycle
        }
    }

    private fun callResultCode(code : Int){
        when (code) {
            200 -> {
                goToMain()
            }
            3000 -> {
                goToOnboarding()
            }
            else -> {}
        }
    }

    private fun goToMain() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    private fun goToOnboarding() {
        findNavController().navigate(R.id.action_loginFragment_to_onboardingFragment)
    }

    private suspend fun saveToken(
        accessToken: String,
        refreshToken: String,
        userProfile: String,
        userNickname: String
    ) {
        lifecycleScope.launch {
            with(tokenManager) {
                saveAccessToken(accessToken)
                saveRefreshToken(refreshToken)
                saveUserProfile(userProfile)
                saveUserNickname(userNickname)
                deleteCountToken()
            }
            homeViewModel.getDistinctHome()
        }
    }

    
}