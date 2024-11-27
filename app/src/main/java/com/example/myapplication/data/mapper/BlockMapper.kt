package com.example.myapplication.data.mapper

import com.example.myapplication.data.repository.remote.api.QuizClearedApi
import com.example.myapplication.data.repository.remote.response.chapter.QuizResponse
import com.example.myapplication.data.repository.remote.response.quiz.Answer
import com.example.myapplication.data.repository.remote.response.quiz.Question
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