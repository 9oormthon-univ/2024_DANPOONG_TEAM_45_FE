package com.example.myapplication.data.repository.remote.request.home

data class PatchHomeDTO(
    val id: Int,
    val characterId: Int,
    val characterType: String,
    val characterName: String
)