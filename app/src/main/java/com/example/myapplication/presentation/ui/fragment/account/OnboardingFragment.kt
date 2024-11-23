package com.example.myapplication.presentation.ui.fragment.account

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.data.repository.remote.request.character.CharacterDTO
import com.example.myapplication.databinding.FragmentOnboardingBinding
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.AccountActivity
import com.example.myapplication.presentation.ui.activity.MainActivity
import com.example.myapplication.presentation.viewmodel.CharacterViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(R.layout.fragment_onboarding) {
    private var currentCount = 0
    private lateinit var characterViewModel: CharacterViewModel

    @Inject
    lateinit var tokenManager: TokenManager
    override fun setLayout() {
        characterViewModel = ViewModelProvider(this)[CharacterViewModel::class.java]
        onClickView()
        textChange()
        observeLifeCycle()
    }

    private fun onClickView() {
        binding.root.setOnClickListener {
            hideKeyboard()
            clearFocus()
        }
        binding.fragmentOnboardingStartBt.setOnClickListener {
            if (it.isSelected) {
                runBlocking {
                    val name = binding.fragmentOnboardingInputNameEt.text.toString()
                    characterViewModel.postCharacter(CharacterDTO(name))
                }
            }
        }
    }

    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                characterViewModel.postCharacter.collectLatest {
                    when (it.result.code) {
                        200 -> {
                            val activity = requireActivity() as AccountActivity
                            activity.startActivityWithClear(MainActivity::class.java)
                        }
                    }
                }
            }
        }

    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun clearFocus() {
        binding.fragmentOnboardingInputNameTl.editText?.clearFocus()
    }

    private fun textChange() {
        binding.fragmentOnboardingInputNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                currentCount = p0?.length ?: 0
                when {
                    // 포커스를 완전히 제거
                    p0.isNullOrEmpty() -> {
                        zeroCount(true)
                    }
                    // 텍스트 길이가 1~6일 때 에러를 지우고, 포커스를 유지
                    p0.length in 1..6 -> {
                        zeroCount(false)
                    }
                    // 텍스트 길이가 7 이상일 때 에러 메시지를 표시
                    p0.length > 6 -> {
                        errorCount()
                    }
                }
                textCountDetail()
            }
        })
    }

    private fun zeroCount(onCount: Boolean) {
        with(binding) {
            if (onCount) {
                fragmentOnboardingBaselineMd.dividerColor =
                    ContextCompat.getColor(requireContext(), R.color.gray8)
                fragmentOnboardingClickCountTv.visibility = View.GONE
                fragmentOnboardingInputNameTl.editText?.clearFocus()  // 모든 포커스를 제거합니다.
                binding.fragmentOnboardingStartBt.isSelected = false
            } else {
                val mintColor = ContextCompat.getColor(requireContext(), R.color.mint1)
                fragmentOnboardingBaselineMd.dividerColor = mintColor
                fragmentOnboardingClickCountTv.visibility = View.VISIBLE
                fragmentOnboardingInputNameEt.requestFocus()  // 포커스를 회복합니다.
                binding.fragmentOnboardingStartBt.isSelected = true
            }
        }
    }

    private fun errorCount() {
        binding.fragmentOnboardingBaselineMd.dividerColor = Color.RED
    }

    private fun textCountDetail() {
        val totalSteps = 6
        val currentStep = currentCount
        val text = String.format("%d / %d", currentStep, totalSteps)
        val spannable = SpannableString(text)
        val slashIndex = text.indexOf('/')
        val endIndex = text.length
        val colorSpan = ForegroundColorSpan(Color.BLACK)
        spannable.setSpan(colorSpan, slashIndex + 1, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.fragmentOnboardingClickCountTv.text = spannable
    }

}