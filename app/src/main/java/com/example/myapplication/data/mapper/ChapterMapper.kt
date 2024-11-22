package com.example.myapplication.data.mapper

import com.example.myapplication.R
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.presentation.ui.fragment.quest.IslandDto

fun AllChapterResponse.toDomain(): List<IslandDto> {
    val item = this.result.mapIndexed { index, it ->
        if (it.id % 2 == 1) {
            IslandDto.IslandLeft(
                name = it.name,
                background = R.drawable.ic_island_left,
                island = locked(it.isCleared, index),
                locked = !it.isCleared,
                id = it.id,
                quizzes = it.quizzes,
                isCleared = it.isCleared
            )
        } else {
            IslandDto.IslandRight(
                name = it.name,
                background = R.drawable.ic_island_right,
                island = locked(it.isCleared, index),
                locked = !it.isCleared,
                id = it.id,
                quizzes = it.quizzes,
                isCleared = it.isCleared
            )
        }
    }
    return item
}


fun locked(isCleared: Boolean, number: Int): Int {
    return if (isCleared) {
        when (number) {
            0 -> R.drawable.iv_biginner_island
            1 -> R.drawable.iv_candy_island_unlocked
            2 -> R.drawable.iv_lake_island_unlocked
            3 -> R.drawable.iv_candy_island_unlocked
            else -> 0
        }
    } else {
        when (number) {
            0 -> R.drawable.iv_biginner_island
            1 -> R.drawable.iv_candy_island_locked
            2 -> R.drawable.iv_lake_island_locked
            3 -> R.drawable.iv_candy_island_locked
            else -> 0
        }
    }
}