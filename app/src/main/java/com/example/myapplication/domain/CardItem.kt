package com.example.myapplication.domain

import com.example.myapplication.R
import com.example.myapplication.data.repository.remote.response.character.CharacterRandomListResponse

data class CardItem(
    val star: String,
    val title: String,
    val image: Int,
    val id: Int
)

fun CharacterRandomListResponse.toCardItem(): List<CardItem> {
    return this.result.mapIndexed { index, item ->
        CardItem(
            star = item.cactusRank,
            id = index,
            image = cactusType(item.cactusName),
            title = item.cactusName
        )
    }
}

fun cactusType(cactusName : String): Int {
    return when (cactusName) {
        "마법사 선인장" -> R.drawable.ic_cactus_magic
        "킹 선인장" -> R.drawable.ic_cactus_3_small
        "영웅 선인장" -> R.drawable.ic_cactus_hero
        else -> 0
    }
}
