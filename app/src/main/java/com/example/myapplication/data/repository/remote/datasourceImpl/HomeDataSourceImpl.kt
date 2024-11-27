package com.example.myapplication.data.repository.remote.datasourceImpl

import android.util.Log
import com.example.myapplication.data.base.BaseLoadingState
import com.example.myapplication.data.repository.remote.api.HomeApi
import com.example.myapplication.data.repository.remote.datasource.remote.HomeDataSource
import com.example.myapplication.data.repository.remote.request.home.PatchHomeDTO
import com.example.myapplication.data.repository.remote.response.BaseResponse
import com.example.myapplication.data.repository.remote.response.CustomException
import com.example.myapplication.data.repository.remote.response.Result
import com.example.myapplication.data.repository.remote.response.home.DistinctHomeIdResponse
import com.example.myapplication.data.repository.remote.response.home.HomeAllList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class HomeDataSourceImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeDataSource {

    override suspend fun postHome(): Flow<BaseResponse<Any>> = flow {
        val result = homeApi.postHome()
        emit(result)
    }.catch { e ->
        Log.e("postHome 에러", e.message.toString())
    }

    override suspend fun getDistinctHome(): Flow<BaseResponse<DistinctHomeIdResponse>> = flow {
        val result = homeApi.getDistinctHome()
        emit(result) // 정상적으로 응답이 왔을 때 emit
    }.catch { e ->
        if (e is HttpException && e.code() == 404) {
            val errorResponse = BaseResponse<DistinctHomeIdResponse>(
                result = Result(
                    code = 3000,
                    message = "홈 정보가 존재하지 않습니다."
                ),
                payload = null,
                status = BaseLoadingState.ERROR // 상태를 ERROR로 설정
            )
            Log.e("getDistinctHome", "HTTP 404: ${errorResponse.result.message}")
            emit(errorResponse) // 404 응답을 Flow로 emit
        } else {
            Log.e("getDistinctHome", "예외 발생: ${e.message}")
            throw e // 기타 예외는 재throw
        }
    }

    override suspend fun deleteHomeId(home_id: String): Flow<BaseResponse<Any>> = flow {
        val result = homeApi.deleteHomeId(home_id)
        emit(result)
    }.catch { e ->
        Log.e("deleteHomeId 에러", e.message.toString())
    }

    override suspend fun patchHomeEdit(
        home_id: String,
        patchHomeDTO: PatchHomeDTO
    ): Flow<BaseResponse<Any>> = flow {
        val result = homeApi.patchHomeEdit(home_id, patchHomeDTO)
        emit(result)
    }.catch { e ->
        Log.e("patchHomeEdit 에러", e.message.toString())
    }

    override suspend fun getAllHome(): Flow<HomeAllList> = flow {
        val result = homeApi.getAllHome()
        emit(result)
    }.catch { e ->
        Log.e("getAllHome 에러", e.message.toString())
    }

    // JSON 파싱 함수
    private fun parseErrorMessage(json: String): String {
        return try {
            val jsonObject = JSONObject(json)
            jsonObject.getJSONObject("result").getString("message")
        } catch (ex: JSONException) {
            "파싱 오류"
        }
    }
}