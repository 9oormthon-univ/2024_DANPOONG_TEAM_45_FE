package com.example.myapplication.presentation.ui.fragment.friend

import android.content.ActivityNotFoundException
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.myapplication.R
import com.example.myapplication.data.repository.remote.response.home.HomePayload
import com.example.myapplication.databinding.FragmentFriendBinding
import com.example.myapplication.domain.model.FriendsEntity
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

    lateinit var item: MutableList<FriendsEntity>
    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                homeViewModel.getAllHome.collectLatest { it ->
                    item = formatItem(it.result.toMutableList())
                    settingItem()
                }
            }
        }
    }

    private fun formatItem(list: MutableList<HomePayload>): MutableList<FriendsEntity> {
        return list.mapIndexed { index, result ->
            result.character
                .toDomain()
                .apply {
                    rank = (index + 1).toString()
                    profile = result.userPicture
                }
        }
            .sortedByDescending { it.point }
            .toMutableList()// 거는거
    }

    private fun settingItem() {
        for (i in item.indices) {
            if (i < 3) {
                setProfileTopRank(i)
            }
        }
        val recycleItem = item.filter { it.rank.toInt() > 3 }
        friendAdapter.submitList(recycleItem)
        Log.d("아이템", "$item")
    }


    private fun setProfileTopRank(index: Int) {
        with(binding) {
            when (index) {
                0 -> {
                    fragmentFriendsFrame1stIv.loadProfileImage(item[index].profile)
                    fragmentFriendsAchieve1Tv.text = item[index].achievement
                    fragmentFriendsName1stTitleTv.text = item[index].name
                }

                1 -> {
                    fragmentFriendsFrame2ndIv.loadProfileImage(item[index].profile)
                    fragmentFriendsAchieve2Tv.text = item[index].achievement
                    fragmentFriendsName2ndTitleTv.text = item[index].name
                }

                2 -> {
                    fragmentFriendsFrame3rdIv.loadProfileImage(item[index].profile)
                    fragmentFriendsAchieve3Tv.text = item[index].achievement
                    fragmentFriendsName3rdTitleTv.text = item[index].name
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
        무무와 함께 코딩 랜드로 떠나 보아요! 🌵
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