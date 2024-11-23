package com.example.myapplication.presentation.ui.fragment.friend

import android.content.ActivityNotFoundException
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFriendBinding
import com.example.myapplication.domain.model.FriendsEntity
import com.example.myapplication.domain.model.calculatorPoint
import com.example.myapplication.domain.model.setLevel
import com.example.myapplication.domain.model.toDomain
import com.example.myapplication.presentation.adapter.FriendAdapter
import com.example.myapplication.presentation.base.BaseFragment
import com.example.myapplication.presentation.viewmodel.HomeViewModel
import com.example.myapplication.presentation.widget.extention.loadProfileImage
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendFragment : BaseFragment<FragmentFriendBinding>(R.layout.fragment_friend) {
    private lateinit var friendAdapter: FriendAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    override fun setLayout() {
        initRecyclerView()
        onClickBtn()
        observeLifeCycle()
    }

    private fun initRecyclerView() {
        friendAdapter = FriendAdapter()
        binding.fragmentFriendsRankRv.adapter = friendAdapter
        homeViewModel.getAllHome()
    }

    lateinit var item: List<FriendsEntity>
    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {

                homeViewModel.getAllHome.collectLatest { it ->
                    item = it.result.mapIndexed { index, result ->
                        result.character.toDomain().apply {
                            profile = result.userPicture
                        }
                    }.sortedByDescending { it.point } // 거는거

                    val recycleItem : MutableList<FriendsEntity> = mutableListOf()
                    for(i in item.indices){
                        if(i >= 3){
                            recycleItem.add(item[i])
                        }
                    }

                    Log.d("로그",item.toString())

                    if (item.isNotEmpty()) {
                        binding.fragmentFriendsFrame1stIv.loadProfileImage(item[0].profile)
                        binding.fragmentFriendsAchieve1Tv.text = item[0].achievement
                        binding.fragmentFriendsName1stTitleTv.text = item[0].name

                        binding.fragmentFriendsFrame2ndIv.loadProfileImage(item[1].profile)
                        binding.fragmentFriendsAchieve2Tv.text = item[1].achievement
                        binding.fragmentFriendsName2ndTitleTv.text = item[1].name

                        binding.fragmentFriendsFrame3rdIv.loadProfileImage(item[2].profile)
                        binding.fragmentFriendsAchieve3Tv.text = item[2].achievement
                        binding.fragmentFriendsName3rdTitleTv.text = item[2].name
                    }
                    friendAdapter.submitList(recycleItem)
                }
            }
        }
    }

    private fun onClickBtn() {
        binding.freagmentFriendsShareIv.setOnClickListener {
            sharedKakaoTalk()
        }
    }

    private fun sharedKakaoTalk() {
        val defaultText = TextTemplate(
            text = """
        무무와 함께 코딩 랜드로 떠나 보아요!
    """.trimIndent(),
            link = Link(
                webUrl = "https://www.naver.com",
                mobileWebUrl = "https://www.naver.com"
            )
        )
        // 피드 메시지 보내기

        val TAG = "카카오톡 쉐어"
        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
            ShareClient.instance.shareDefault(
                requireContext(),
                defaultText
            ) { sharingResult, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡 공유 실패", error)
                } else if (sharingResult != null) {
                    Log.d(TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
                    startActivity(sharingResult.intent)
                    Log.w(TAG, "Warning Msg: ${sharingResult.warningMsg}")
                    Log.w(TAG, "Argument Msg: ${sharingResult.argumentMsg}")
                }
            }
        } else {
            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultText)
            try {
                KakaoCustomTabsClient.openWithDefault(requireContext(), sharerUrl)
            } catch (e: UnsupportedOperationException) {

            }
            try {
                KakaoCustomTabsClient.open(requireContext(), sharerUrl)
            } catch (e: ActivityNotFoundException) {
            }
        }
    }

}