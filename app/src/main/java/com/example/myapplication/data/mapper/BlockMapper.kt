package com.example.myapplication.data.mapper

import  com.example.myapplication.data.repository.remote.response.home.Character
import com.example.myapplication.data.repository.remote.response.quiz.Answer
import com.example.myapplication.data.repository.remote.response.quiz.Question
import com.example.myapplication.domain.model.FriendsEntity
import com.example.myapplication.presentation.ui.activity.BlockDTO

fun Question.toBlockDTO(): BlockDTO {
    return BlockDTO(
        blockType = this.type,
        blockDescript = this.msg,
        repeat = this.repeat
    )
}


fun Answer.toBlockDTO(): BlockDTO {
    return BlockDTO(
        blockType = this.type,
        blockDescript = this.msg,
        repeat = this.repeat
    )
}

fun List<Question>.toBlockDTOList(): List<BlockDTO> {
    return this.map { it.toBlockDTO() }
}


fun Character.toDomain(): FriendsEntity {
    return FriendsEntity(
        name = this.name,
        achievement = "${this.cactusName} ${calculatorPoint(this.activityPoints)}%",
        point = activityPoints
    )
}

fun setLevel(type: String): String {
    return when (type) {
        "LEVEL_LOW" -> "미니 선인장"
        "LEVEL_MEDIUM" -> "꽃 선인장"
        "LEVEL_HIGH" -> "킹 선인장"
        else -> { "히어로 선인장" }
    }
}

fun calculatorPoint(point: Int): Int {
    return point % 100
}