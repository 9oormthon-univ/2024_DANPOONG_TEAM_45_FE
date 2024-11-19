package com.example.myapplication.data.repository.remote.response.home

import com.example.myapplication.data.base.Result

data class DistinctHomeIdResponse(
    val result: Result = Result(),
    val payload : HomePayload = HomePayload()
)
