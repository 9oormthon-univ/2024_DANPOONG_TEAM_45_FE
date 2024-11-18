package com.example.myapplication.presentation.ui.fragment.account

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.codingland.presenter.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.data.repository.remote.request.login.LogInKakaoDto
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.presentation.ui.activity.MainActivity
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


    override fun setLayout() {
        binding.differentLoginTv.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_onboardingFragment)
        }

        initViewModel()
        setOnclickBtn()
        observeLifeCycle()
    }

    //뷰 모델 초기화
    private fun initViewModel() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    //카카오 로그인 버튼
    private fun setOnclickBtn() {
        binding.activityAccountKakaoLoginBt.setOnClickListener {
            userKakaoLogin()
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
                runBlocking {
                    tokenManager.deleteAccessToken()
                    loginViewModel.postKakaoLogin(sendKakaoAccessToken(token.accessToken))
                    Log.i("카카오", "로그인 성공 ${token.accessToken}")
                }
            }
        }
    }

    //토큰 수령 시 다음 화면으로 넘어감
    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                loginViewModel.kakaoLogin.collectLatest {
                    if (it.result.code == 200) {
                        with(it.payload) {
                            saveToken(accessToken, refreshToken, picture, nickname)
                        }
                        Log.i("카카오", "로그인 성공 $it")
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        Log.i("카카오", "Intent 실행 완료")
                    }
                }
            }
        }
    }

    private fun saveToken(
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
            }
        }
    }

}