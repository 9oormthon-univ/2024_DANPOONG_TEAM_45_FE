package com.example.myapplication.presentation.ui.fragment.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentEncyclopediaBinding
import com.example.myapplication.domain.toCardItem
import com.example.myapplication.presentation.adapter.CardAdapter
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.ui.activity.HeroCactusActivity
import com.example.myapplication.presentation.ui.fragment.quest.ItemClickListener
import com.example.myapplication.presentation.viewmodel.CharacterViewModel
import com.example.myapplication.presentation.widget.extention.TokenManager
import com.kakao.sdk.share.ShareClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class EncyclopediaFragament :
    BaseFragment<FragmentEncyclopediaBinding>(R.layout.fragment_encyclopedia), ItemClickListener {

    @Inject
    lateinit var tokenManager: TokenManager
    lateinit var cardAdapter: CardAdapter
    private lateinit var characterViewModel: CharacterViewModel
    var clickItemId = 0

    override fun setLayout() {
        initAdapter()
        initViewModel()
        observeLifeCycle()
        setOnClickBtn()
    }

    private fun initViewModel() {
        characterViewModel = ViewModelProvider(this)[CharacterViewModel::class.java]
        characterViewModel.getGuideBook()
    }

    private fun setOnClickBtn() {

        binding.fragmentEncyclopediaArrowBackIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.fragmentShareCardBt.setOnClickListener {
            shareToKakaoWithImage()
        }

    }


    private fun initAdapter() {
        cardAdapter = CardAdapter(this)
        // 레이아웃 매니저 명시적으로 설정
        binding.fragmentEncyclopediaCardRv.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // 데코레이션 추가
        val marginInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            64f,
            resources.displayMetrics
        ).toInt()

        binding.fragmentEncyclopediaCardRv.addItemDecoration(
            HorizontalMarginItemDecoration(
                firstItemMargin = marginInPx,
                lastItemMargin = marginInPx
            )
        )
        binding.fragmentEncyclopediaCardRv.adapter = cardAdapter
        cardAdapter.submitList(listOf())
    }

    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                characterViewModel.getGuideBook.collectLatest {
                    when (it.result.code) {
                        200 -> {
                            val size = (it.payload?.result?.size ?: 0).toString()
                            binding.fragmentEncyclopediaTv.text = size
                            if (size.toInt() == 0) {
                                binding.fragmentEncyclopediaCardRv.visibility = View.GONE
                                binding.fragmentShareCardBt.visibility = View.GONE
                            } else {
                                binding.fragmentEncyclopediaCardRv.visibility = View.VISIBLE
                                binding.fragmentShareCardBt.visibility = View.VISIBLE
                                val list = it.payload?.toCardItem()
                                val marginInPx = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    16f,
                                    resources.displayMetrics
                                ).toInt()
                                binding.fragmentEncyclopediaCardRv.addItemDecoration(
                                    HorizontalMarginItemDecoration(
                                        firstItemMargin = marginInPx,  // in pixels
                                        lastItemMargin = marginInPx    // in pixels
                                    )
                                )
                                cardAdapter.submitList(list)
                            }
                        }
                    }
                }
            }
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
        // RecyclerView의 특정 아이템 ViewHolder를 가져오기
        val viewHolder =
            binding.fragmentEncyclopediaCardRv.findViewHolderForAdapterPosition(clickItemId)

        // ViewHolder의 아이템 뷰를 가져오기
        val itemView =
            viewHolder?.itemView?.findViewById<FrameLayout>(R.id.framelayout_my_cactus_card)

        if (itemView != null) {
            // FrameLayout -> Bitmap 변환
            val bitmap = captureViewToBitmap(itemView)

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
                    ) { result, errors ->
                        if (errors != null) {
                            Log.e("카카오톡 공유", "공유 실패", errors)
                        } else if (result != null) {
                            startActivity(result.intent) // 카카오톡 실행
                        }
                    }
                }
            }
        } else {
            Log.e("Share", "캡처할 아이템 뷰를 찾을 수 없음")
        }
    }

    override fun click(item: Any) {
        clickItemId = item as Int
    }

}
