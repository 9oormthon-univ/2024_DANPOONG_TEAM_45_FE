package com.example.myapplication.data.repository.remote.response.home

data class DistinctHomeIdResponse(
    val id: Int = 0,
    val character: DistinctCharacterResponse = DistinctCharacterResponse()
)
