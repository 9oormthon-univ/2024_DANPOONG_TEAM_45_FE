package com.example.myapplication.data.repository.remote.response.login

import com.example.myapplication.data.base.BaseLoadingState
import com.example.myapplication.data.base.Result

data class LogInKakaoResponse(
    val result: Result = Result(),
    val payload: Payload = Payload(),
    var state : BaseLoadingState = BaseLoadingState.IDLE
)
