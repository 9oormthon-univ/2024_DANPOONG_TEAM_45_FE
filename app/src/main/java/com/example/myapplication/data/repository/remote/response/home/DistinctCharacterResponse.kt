package com.example.myapplication.data.repository.remote.response.home

data class DistinctCharacterResponse(
    val id: Int = 0,
    val name: String = "",
    val level: Int = 0,
    val type: String = "",
    val cactusType: String = "",
    val activityPoints: Int = 0
)

data class DistinctCharacter(
    val id: Int = 0,
    val character: Character = Character(),
    val userPicture: String = ""
)

data class Character(
    val id: Int = 0,
    val name: String = "",
    val level: Int = 0,
    val type: String? = null,
    val cactusName: String = "",
    val cactusRank: String = "",
    val activityPoints: Int = 0
)