package com.example.myapplication.domain.model

import com.example.myapplication.data.repository.remote.response.home.DistinctCharacterResponse
import com.example.myapplication.domain.model.home.CharacterType

data class FriendsEntity(
    var rank: String = "",
    var profile: String = "",
    val name: String = "",
    val achievement: String = "",
    val point : Int = 0
)

