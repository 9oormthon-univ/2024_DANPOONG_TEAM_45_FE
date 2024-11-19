package com.example.myapplication.data.repository.remote.request.home

data class PatchHomeDTO(
    val characterId: Int,
    val characterName: String,
    val characterType: String,
    val id: Int
)