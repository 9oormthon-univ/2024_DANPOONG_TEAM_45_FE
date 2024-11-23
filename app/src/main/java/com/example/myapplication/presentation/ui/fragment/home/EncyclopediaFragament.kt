package com.example.myapplication.presentation.ui.fragment.home

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.widget.FrameLayout
import androidx.core.content.FileProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentEncyclopediaBinding
import com.example.myapplication.presentation.base.BaseFragment
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import java.io.File
import java.io.FileOutputStream

class EncyclopediaFragament : BaseFragment<FragmentEncyclopediaBinding>(R.layout.fragment_encyclopedia) {
    override fun setLayout() {
        binding.fragmentEncyclopediaArrowBackIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.fragmentShareCardBt.setOnClickListener {
            shareViewAsImage(requireContext(), binding.framelayoutMyCactusCard)
        }
    }


    private fun captureViewToBitmap(view: FrameLayout): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveBitmapToFile(bitmap: Bitmap, context: Context): File {
        val cacheDir = File(context.cacheDir, "images")
        if (!cacheDir.exists()) cacheDir.mkdirs()
        val file = File(cacheDir, "shared_view.png")

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // PNG로 저장
        }
        return file
    }

    private fun shareViewAsImage(context: Context, view: FrameLayout) {
        // FrameLayout을 Bitmap으로 변환
        val bitmap = captureViewToBitmap(view)

        // Bitmap을 파일로 저장
        val imageFile = saveBitmapToFile(bitmap, context)

        // FileProvider로 URI 가져오기
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )

        // 공유 Intent 생성
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri) // 공유할 이미지 URI
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // 권한 부여
        }

        try {
            // 카카오톡으로 공유 실행
            context.startActivity(
                Intent.createChooser(shareIntent, "카카오톡으로 공유하기")
            )
        } catch (e: ActivityNotFoundException) {
            // 카카오톡이 없을 때 예외 처리
            Log.e("ShareView", "카카오톡이 설치되어 있지 않습니다.")
        }
    }

}