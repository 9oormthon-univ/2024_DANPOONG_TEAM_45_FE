package com.example.myapplication.presentation.ui.fragment.quest

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.myapplication.databinding.DialogSuccessBinding

class CustomDialog(
    private val dialogClickListener: DialogClickListener,
    private val title: String,
    private val subtitle: String
) : DialogFragment(
) {
    private var _binding: DialogSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSuccessBinding.inflate(inflater, container, false)
        val view = binding.root
        setDialogWindowSetting()
        onBindingTitle()
        setOnClickTwoBtn()
        return view
    }

    private fun setDialogWindowSetting(){
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        isCancelable = false
    }

    private fun onBindingTitle(){
        binding.dialogButtonTwoTitle.text = title
        binding.dialogButtonTwoSubtitle.text = subtitle
    }

    private fun setOnClickTwoBtn(){
        binding.dialogButtonStop.setOnClickListener {
            dialogClickListener.onClickStop()
            dismiss()
        }

        binding.dialogButtonNextStep.setOnClickListener {
            dialogClickListener.onClickNext()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}