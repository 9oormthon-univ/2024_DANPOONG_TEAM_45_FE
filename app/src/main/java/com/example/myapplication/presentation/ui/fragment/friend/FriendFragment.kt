package com.example.myapplication.presentation.ui.fragment.friend

import android.content.ActivityNotFoundException
import android.util.Log
import com.example.codingland.presenter.base.BaseFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFriendBinding
import com.example.myapplication.domain.model.FriendsEntity
import com.example.myapplication.presentation.adapter.FriendAdapter
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendFragment : BaseFragment<FragmentFriendBinding>(R.layout.fragment_friend) {
    private lateinit var friendAdapter: FriendAdapter

    override fun setLayout() {
        initRecyclerView()
        onClickBtn()
    }

    private fun initRecyclerView() {
        friendAdapter = FriendAdapter()
        binding.fragmentFriendsRankRv.adapter = friendAdapter
        friendAdapter.submitList(
            listOf(
                FriendsEntity(
                    "4",
                    "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBwgQChENDRAPDQ8QDQ0NEBANDQ8NDQ8PFBEWFhUdExMZHCggGBolGxUTITEhJSkrLi42Fx85ODMsNygtLisBCgoKDg0OFxAQFS4dICU3NSsrLS0rMC0rLSstKzctLSs1Ky03KystNS0rKy0tLS0rKysrKzc3LS03KysrKysrN//AABEIAOAA4QMBIgACEQEDEQH/xAAaAAEBAQADAQAAAAAAAAAAAAAAAQUCBAYD/8QAMRABAAIAAwYCCQUBAQAAAAAAAAECAwQRBRIhMUFRMnEiYXKRkqGxweEzQlKB0SMU/8QAGQEBAQEBAQEAAAAAAAAAAAAAAAEDAgQF/8QAHhEBAQEBAAIDAQEAAAAAAAAAAAECEQMxIUFREhP/2gAMAwEAAhEDEQA/APVgr6L5qCoKKIIoAAAAAAAAAAAAAIKAgoCCgIKAgoCKAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAKogCiKAAIAAAAAAAAAAAACAqiAKIAogAqAgAKAAKigACAAAAA5Uw72nSsTPlGr71yGYnpEecwlsiyWusO1Oz8x2ifK0PhiYOLXxVmP64e87DlcAFQABABQAAAAAAAQAFAAFRQABAACIno0crs+OeJ8P8Arns7KxEb9uc8vVDustb+o1zj7qVrERpEREdo4KDNqAA6eZyFLcaejPb9s/4y71tE6TGkw9A62dy0XrrHijl6/U0zv9Z6x+McBqxQAUAAAAAAAEABQABUUAAQfbJ4W/ixHTnPlD4tDZFeNreUOdXkdZna0QGD0AAAAAAMjaWFu4uscrcf76uq1Nq1/wCcT2t9YZbfN7GG5yoA6cgAAAAAAAAAAACooAAg09k+C3tR9GY72yr6XmveNfc536d49tMBg3AAAAAAdXaf6M+1VkNLa1/RrX17zNbY9MN+0AduQAAAAAAAQAFAAFRQABBywsSa3i0c4nVxBW/h3rasWjlMauTHyWamk6TxrPylr1tExrE6xPWGGs8b511QHLoAAJmIjWeEDLz+c3vQp4es9/wsnXOtcdfNY2/iTbpyjyfIG7BAFAAAAAAAAQAFAAFRQABAAB9cDMYlJ9GeHWJ5S+Qi9auFtHCnxa1n3w7FcfBnlavvhhI5uI7nkrfnGwo52r8UPhi5/AjlO9Pq/wBY6p/nC+SuxmM5iX4eGvaPu64O5OOLegCogAoAAAAAAAIACgACooAAgPtl8tiXnhwjrM8mngZPCpx03p7z9nN1I7zm1mYWUxrcq6R3nhDt4ezP5W+GPu0Bnd1pMR1a7Py8dJnzmXOMnl/4R833HPa6/mfj4/8Ajy/8I+bhbIZeekx5TLsh2n8z8Z99mR+23xQ6uLk8evONY714todTdc3EeeG1j5TCvzjSe8cJ/LMzOVxKc+Ne8fdpNSs9YsdcB05AAAAAAABAAUAAVFAdvJZOb+lbhX52/Djkctv21nwxz9c9mxEQz3rnxHeM9+aViIjSOER0gBk2AAAAAAAACYjTSeIAy89kt306eHrHb8Oi9GyNoZXdner4Z+Utca+qy1n7jpgNGYAAAAAIACgADlSk2tFY5zOji0NlYWtpvPThHmlvIsna7+Dh1rSKx0+cuYPO9AAAAAAAAAAAACuOJStqzWeUxo5Ajz+PhTS81np84cGntbC9GLx04T5dGY3zexhqcoA6QAAAEABQABt5Gm7g19cb3vYkRxehrGkRHaIhn5Gnj9qAyagAAAAAAAAAAAKAI4Y9N7DtXvE+9596N5/MV0xLR2tP1aeNn5HABqzAAABAAUABywvHX2o+r0Dz+F46+1X6vQMvI18f2AM2gAAAAAAAAAAACgCDCzv69/a+zdYee/Xv5/Z34/bjfp8AGzIAAAB//9k=",
                    "김수진",
                    "킹 선인장 %d%"
                ),
                FriendsEntity(
                    "5",
                    "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBwgQChENDRAPDQ8QDQ0NEBANDQ8NDQ8PFBEWFhUdExMZHCggGBolGxUTITEhJSkrLi42Fx85ODMsNygtLisBCgoKDg0OFxAQFS4dICU3NSsrLS0rMC0rLSstKzctLSs1Ky03KystNS0rKy0tLS0rKysrKzc3LS03KysrKysrN//AABEIAOAA4QMBIgACEQEDEQH/xAAaAAEBAQADAQAAAAAAAAAAAAAAAQUCBAYD/8QAMRABAAIAAwYCCQUBAQAAAAAAAAECAwQRBRIhMUFRMnEiYXKRkqGxweEzQlKB0SMU/8QAGQEBAQEBAQEAAAAAAAAAAAAAAAEDAgQF/8QAHhEBAQEBAAIDAQEAAAAAAAAAAAECEQMxIUFREhP/2gAMAwEAAhEDEQA/APVgr6L5qCoKKIIoAAAAAAAAAAAAAIKAgoCCgIKAgoCKAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAKogCiKAAIAAAAAAAAAAAACAqiAKIAogAqAgAKAAKigACAAAAA5Uw72nSsTPlGr71yGYnpEecwlsiyWusO1Oz8x2ifK0PhiYOLXxVmP64e87DlcAFQABABQAAAAAAAQAFAAFRQABAACIno0crs+OeJ8P8Arns7KxEb9uc8vVDustb+o1zj7qVrERpEREdo4KDNqAA6eZyFLcaejPb9s/4y71tE6TGkw9A62dy0XrrHijl6/U0zv9Z6x+McBqxQAUAAAAAAAEABQABUUAAQfbJ4W/ixHTnPlD4tDZFeNreUOdXkdZna0QGD0AAAAAAMjaWFu4uscrcf76uq1Nq1/wCcT2t9YZbfN7GG5yoA6cgAAAAAAAAAAACooAAg09k+C3tR9GY72yr6XmveNfc536d49tMBg3AAAAAAdXaf6M+1VkNLa1/RrX17zNbY9MN+0AduQAAAAAAAQAFAAFRQABBywsSa3i0c4nVxBW/h3rasWjlMauTHyWamk6TxrPylr1tExrE6xPWGGs8b511QHLoAAJmIjWeEDLz+c3vQp4es9/wsnXOtcdfNY2/iTbpyjyfIG7BAFAAAAAAAAQAFAAFRQABAAB9cDMYlJ9GeHWJ5S+Qi9auFtHCnxa1n3w7FcfBnlavvhhI5uI7nkrfnGwo52r8UPhi5/AjlO9Pq/wBY6p/nC+SuxmM5iX4eGvaPu64O5OOLegCogAoAAAAAAAIACgACooAAgPtl8tiXnhwjrM8mngZPCpx03p7z9nN1I7zm1mYWUxrcq6R3nhDt4ezP5W+GPu0Bnd1pMR1a7Py8dJnzmXOMnl/4R833HPa6/mfj4/8Ajy/8I+bhbIZeekx5TLsh2n8z8Z99mR+23xQ6uLk8evONY714todTdc3EeeG1j5TCvzjSe8cJ/LMzOVxKc+Ne8fdpNSs9YsdcB05AAAAAAABAAUAAVFAdvJZOb+lbhX52/Djkctv21nwxz9c9mxEQz3rnxHeM9+aViIjSOER0gBk2AAAAAAAACYjTSeIAy89kt306eHrHb8Oi9GyNoZXdner4Z+Utca+qy1n7jpgNGYAAAAAIACgADlSk2tFY5zOji0NlYWtpvPThHmlvIsna7+Dh1rSKx0+cuYPO9AAAAAAAAAAAACuOJStqzWeUxo5Ajz+PhTS81np84cGntbC9GLx04T5dGY3zexhqcoA6QAAAEABQABt5Gm7g19cb3vYkRxehrGkRHaIhn5Gnj9qAyagAAAAAAAAAAAKAI4Y9N7DtXvE+9596N5/MV0xLR2tP1aeNn5HABqzAAABAAUABywvHX2o+r0Dz+F46+1X6vQMvI18f2AM2gAAAAAAAAAAACgCDCzv69/a+zdYee/Xv5/Z34/bjfp8AGzIAAAB//9k=",
                    "김수진",
                    "킹 선인장 %d%"
                ),
                FriendsEntity(
                    "6",
                    "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBwgQChENDRAPDQ8QDQ0NEBANDQ8NDQ8PFBEWFhUdExMZHCggGBolGxUTITEhJSkrLi42Fx85ODMsNygtLisBCgoKDg0OFxAQFS4dICU3NSsrLS0rMC0rLSstKzctLSs1Ky03KystNS0rKy0tLS0rKysrKzc3LS03KysrKysrN//AABEIAOAA4QMBIgACEQEDEQH/xAAaAAEBAQADAQAAAAAAAAAAAAAAAQUCBAYD/8QAMRABAAIAAwYCCQUBAQAAAAAAAAECAwQRBRIhMUFRMnEiYXKRkqGxweEzQlKB0SMU/8QAGQEBAQEBAQEAAAAAAAAAAAAAAAEDAgQF/8QAHhEBAQEBAAIDAQEAAAAAAAAAAAECEQMxIUFREhP/2gAMAwEAAhEDEQA/APVgr6L5qCoKKIIoAAAAAAAAAAAAAIKAgoCCgIKAgoCKAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAKogCiKAAIAAAAAAAAAAAACAqiAKIAogAqAgAKAAKigACAAAAA5Uw72nSsTPlGr71yGYnpEecwlsiyWusO1Oz8x2ifK0PhiYOLXxVmP64e87DlcAFQABABQAAAAAAAQAFAAFRQABAACIno0crs+OeJ8P8Arns7KxEb9uc8vVDustb+o1zj7qVrERpEREdo4KDNqAA6eZyFLcaejPb9s/4y71tE6TGkw9A62dy0XrrHijl6/U0zv9Z6x+McBqxQAUAAAAAAAEABQABUUAAQfbJ4W/ixHTnPlD4tDZFeNreUOdXkdZna0QGD0AAAAAAMjaWFu4uscrcf76uq1Nq1/wCcT2t9YZbfN7GG5yoA6cgAAAAAAAAAAACooAAg09k+C3tR9GY72yr6XmveNfc536d49tMBg3AAAAAAdXaf6M+1VkNLa1/RrX17zNbY9MN+0AduQAAAAAAAQAFAAFRQABBywsSa3i0c4nVxBW/h3rasWjlMauTHyWamk6TxrPylr1tExrE6xPWGGs8b511QHLoAAJmIjWeEDLz+c3vQp4es9/wsnXOtcdfNY2/iTbpyjyfIG7BAFAAAAAAAAQAFAAFRQABAAB9cDMYlJ9GeHWJ5S+Qi9auFtHCnxa1n3w7FcfBnlavvhhI5uI7nkrfnGwo52r8UPhi5/AjlO9Pq/wBY6p/nC+SuxmM5iX4eGvaPu64O5OOLegCogAoAAAAAAAIACgACooAAgPtl8tiXnhwjrM8mngZPCpx03p7z9nN1I7zm1mYWUxrcq6R3nhDt4ezP5W+GPu0Bnd1pMR1a7Py8dJnzmXOMnl/4R833HPa6/mfj4/8Ajy/8I+bhbIZeekx5TLsh2n8z8Z99mR+23xQ6uLk8evONY714todTdc3EeeG1j5TCvzjSe8cJ/LMzOVxKc+Ne8fdpNSs9YsdcB05AAAAAAABAAUAAVFAdvJZOb+lbhX52/Djkctv21nwxz9c9mxEQz3rnxHeM9+aViIjSOER0gBk2AAAAAAAACYjTSeIAy89kt306eHrHb8Oi9GyNoZXdner4Z+Utca+qy1n7jpgNGYAAAAAIACgADlSk2tFY5zOji0NlYWtpvPThHmlvIsna7+Dh1rSKx0+cuYPO9AAAAAAAAAAAACuOJStqzWeUxo5Ajz+PhTS81np84cGntbC9GLx04T5dGY3zexhqcoA6QAAAEABQABt5Gm7g19cb3vYkRxehrGkRHaIhn5Gnj9qAyagAAAAAAAAAAAKAI4Y9N7DtXvE+9596N5/MV0xLR2tP1aeNn5HABqzAAABAAUABywvHX2o+r0Dz+F46+1X6vQMvI18f2AM2gAAAAAAAAAAACgCDCzv69/a+zdYee/Xv5/Z34/bjfp8AGzIAAAB//9k=",
                    "김수진",
                    "킹 선인장 %d%"
                ),
                FriendsEntity(
                    "7",
                    "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBwgQChENDRAPDQ8QDQ0NEBANDQ8NDQ8PFBEWFhUdExMZHCggGBolGxUTITEhJSkrLi42Fx85ODMsNygtLisBCgoKDg0OFxAQFS4dICU3NSsrLS0rMC0rLSstKzctLSs1Ky03KystNS0rKy0tLS0rKysrKzc3LS03KysrKysrN//AABEIAOAA4QMBIgACEQEDEQH/xAAaAAEBAQADAQAAAAAAAAAAAAAAAQUCBAYD/8QAMRABAAIAAwYCCQUBAQAAAAAAAAECAwQRBRIhMUFRMnEiYXKRkqGxweEzQlKB0SMU/8QAGQEBAQEBAQEAAAAAAAAAAAAAAAEDAgQF/8QAHhEBAQEBAAIDAQEAAAAAAAAAAAECEQMxIUFREhP/2gAMAwEAAhEDEQA/APVgr6L5qCoKKIIoAAAAAAAAAAAAAIKAgoCCgIKAgoCKAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAKogCiKAAIAAAAAAAAAAAACAqiAKIAogAqAgAKAAKigACAAAAA5Uw72nSsTPlGr71yGYnpEecwlsiyWusO1Oz8x2ifK0PhiYOLXxVmP64e87DlcAFQABABQAAAAAAAQAFAAFRQABAACIno0crs+OeJ8P8Arns7KxEb9uc8vVDustb+o1zj7qVrERpEREdo4KDNqAA6eZyFLcaejPb9s/4y71tE6TGkw9A62dy0XrrHijl6/U0zv9Z6x+McBqxQAUAAAAAAAEABQABUUAAQfbJ4W/ixHTnPlD4tDZFeNreUOdXkdZna0QGD0AAAAAAMjaWFu4uscrcf76uq1Nq1/wCcT2t9YZbfN7GG5yoA6cgAAAAAAAAAAACooAAg09k+C3tR9GY72yr6XmveNfc536d49tMBg3AAAAAAdXaf6M+1VkNLa1/RrX17zNbY9MN+0AduQAAAAAAAQAFAAFRQABBywsSa3i0c4nVxBW/h3rasWjlMauTHyWamk6TxrPylr1tExrE6xPWGGs8b511QHLoAAJmIjWeEDLz+c3vQp4es9/wsnXOtcdfNY2/iTbpyjyfIG7BAFAAAAAAAAQAFAAFRQABAAB9cDMYlJ9GeHWJ5S+Qi9auFtHCnxa1n3w7FcfBnlavvhhI5uI7nkrfnGwo52r8UPhi5/AjlO9Pq/wBY6p/nC+SuxmM5iX4eGvaPu64O5OOLegCogAoAAAAAAAIACgACooAAgPtl8tiXnhwjrM8mngZPCpx03p7z9nN1I7zm1mYWUxrcq6R3nhDt4ezP5W+GPu0Bnd1pMR1a7Py8dJnzmXOMnl/4R833HPa6/mfj4/8Ajy/8I+bhbIZeekx5TLsh2n8z8Z99mR+23xQ6uLk8evONY714todTdc3EeeG1j5TCvzjSe8cJ/LMzOVxKc+Ne8fdpNSs9YsdcB05AAAAAAABAAUAAVFAdvJZOb+lbhX52/Djkctv21nwxz9c9mxEQz3rnxHeM9+aViIjSOER0gBk2AAAAAAAACYjTSeIAy89kt306eHrHb8Oi9GyNoZXdner4Z+Utca+qy1n7jpgNGYAAAAAIACgADlSk2tFY5zOji0NlYWtpvPThHmlvIsna7+Dh1rSKx0+cuYPO9AAAAAAAAAAAACuOJStqzWeUxo5Ajz+PhTS81np84cGntbC9GLx04T5dGY3zexhqcoA6QAAAEABQABt5Gm7g19cb3vYkRxehrGkRHaIhn5Gnj9qAyagAAAAAAAAAAAKAI4Y9N7DtXvE+9596N5/MV0xLR2tP1aeNn5HABqzAAABAAUABywvHX2o+r0Dz+F46+1X6vQMvI18f2AM2gAAAAAAAAAAACgCDCzv69/a+zdYee/Xv5/Z34/bjfp8AGzIAAAB//9k=",
                    "김수진",
                    "킹 선인장 %d%"
                )
            )
        )
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