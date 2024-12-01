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

fun DistinctCharacterResponse.toDomain(): FriendsEntity {
    return FriendsEntity(
        name = this.name,
        achievement = "${setLevel(this.type)} ${calculatorPoint(this.activityPoints)}%",
        point = activityPoints
    )
}

fun setLevel(type: String): String {
    return when (type) {
        "LEVEL_LOW" -> "미니 선인장"
        "LEVEL_MEDIUM" -> "꽃 선인장"
        "LEVEL_HIGH" -> "킹 선인장"
        else -> { "" }
    }
}

fun calculatorPoint(point: Int): Int {
    return point % 100
}