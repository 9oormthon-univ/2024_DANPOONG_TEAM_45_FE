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
import com.kakao.sdk.share.ShareClient
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
            shareToKakaoWithImage()
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

    private fun shareToKakaoWithImage() {
        // FrameLayout -> Bitmap 변환
        val bitmap = captureViewToBitmap(binding.framelayoutMyCactusCard)

        // Bitmap을 캐시 파일로 저장
        val file = saveBitmapToFile(bitmap, requireContext())

        // 카카오 이미지 업로드
        ShareClient.instance.uploadImage(file) { imageResult, error ->
            if (error != null) {
                Log.e("카카오 이미지 업로드", "이미지 업로드 실패", error)
            } else if (imageResult != null) {
                val imageUrl = imageResult.infos.original.url // 업로드된 이미지 URL

                // FeedTemplate 생성 (이미지와 텍스트 포함)
                val feedTemplate = com.kakao.sdk.template.model.FeedTemplate(
                    content = com.kakao.sdk.template.model.Content(
                        title = "코딩 랜드로 떠나 보아요! 🌵",
                        description = "무무와 함께 코딩 랜드 모험을 즐기세요!",
                        imageUrl = imageUrl, // 업로드된 이미지 URL
                        link = com.kakao.sdk.template.model.Link(
                            webUrl = "https://www.naver.com",
                            mobileWebUrl = "https://www.naver.com"
                        )
                    ),
                    buttons = listOf(
                        com.kakao.sdk.template.model.Button(
                            title = "더 알아보기",
                            link = com.kakao.sdk.template.model.Link(
                                webUrl = "https://www.naver.com",
                                mobileWebUrl = "https://www.naver.com"
                            )
                        )
                    )
                )

                // 카카오톡으로 메시지 전송
                ShareClient.instance.shareDefault(
                    requireContext(),
                    feedTemplate
                ) { result, error ->
                    if (error != null) {
                        Log.e("카카오톡 공유", "공유 실패", error)
                    } else if (result != null) {
                        startActivity(result.intent) // 카카오톡 실행
                    }
                }
            }
        }
    }
}