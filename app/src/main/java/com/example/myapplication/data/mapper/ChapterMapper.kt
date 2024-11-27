package com.example.myapplication.data.mapper

import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.data.repository.remote.response.chapter.AllChapterResponse
import com.example.myapplication.data.repository.remote.response.chapter.DistinctChapterResponse
import com.example.myapplication.data.repository.remote.response.chapter.QuizResponse
import com.example.myapplication.presentation.ui.fragment.quest.IslandDto
import com.example.myapplication.presentation.ui.fragment.quest.QuestDto

fun AllChapterResponse.toDomain(): List<IslandDto> {
    val item = mutableListOf<IslandDto>() // 결과를 저장할 리스트

    this.result.forEachIndexed { index, it ->
        if (it.id % 2 == 1) {
            item.add( // IslandLeft 추가
                IslandDto.IslandLeft(
                    name = it.name,
                    background = R.drawable.ic_island_left,
                    island = locked(decideIsClear(this.result, it.id), index),
                    locked = !decideIsClear(this.result, it.id),
                    id = it.id,
                    quizzes = it.quizzes,
                    isCleared = it.isCleared
                )
            )
        } else {
            item.add( // IslandRight 추가
                IslandDto.IslandRight(
                    name = it.name,
                    background = R.drawable.ic_island_right,
                    island = locked(decideIsClear(this.result, it.id), index), // 사진
                    locked = !decideIsClear(this.result, it.id), // 잠김 여부
                    id = it.id,
                    quizzes = it.quizzes,
                    isCleared = it.isCleared
                )
            )
        }
    }

    return item
}

fun decideIsClear(list: List<DistinctChapterResponse>, id: Int): Boolean {
    // 첫 번째 챕터는 항상 잠금 해제
    if (id == 1) {
        return false
    }
    // 이전 챕터의 상태 확인
    val previousCleared = list[id - 2].isCleared // id - 2로 이전 챕터 참조
    return previousCleared
}

fun locked(isCleared: Boolean, number: Int): Int {
    Log.d("okhttp", "locked() 호출 - isCleared: $isCleared, number: $number")
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

fun QuizResponse.toDomain(): QuestDto {
    val item = QuestDto(
        gameName = titles[this.quizId - 1],
        gameType = decideType(this.quizId),
        gameImg = images[this.quizId - 1],
        gameState = decideClear(this.isCleared),
        gameDescript = subtitles[this.quizId - 1],
        id = this.quizId,
        isCleared = this.isCleared,
        isOpen = false
    )
    return item
}


fun decideType(id: Int): String {
    return if (id == 1) {
        "NORMAL"
    } else {
        "BLOCK"
    }
}

fun decideClear(isCleared: Boolean): Int {
    return if (isCleared) {
        2
    } else {
        0
    }
}

val titles = mutableListOf(
    "기초 훈련하기",
    "모험 준비하기",
    "달콤한 첫 걸음",
    "사탕을 찾아가자!",
    "껌을 피하는 법",
    "불 진압하기",
    "사탕의 섬을 구해라!",
    "바위점프!",
    "모험 준비하기",
    "모험의 끝으로"
)

val subtitles = mutableListOf(
    "초보 모험가를 위한 기초 훈련!",
    "본격적으로 모험을 준비해봐요!",
    "사탕의 섬에서의 첫 퀘스트!",
    "무무가 사탕을 찾도록 도와주세요",
    "무무가 껌을 밟지 않도록 도와주세요",
    "사탕의 섬에 불이 났어요!",
    "사탕의 섬이 녹아내리고 있어요",
    "초보 모험가를 위한 기초 훈련!",
    "본격적으로 모험을 준비해봐요!"
)

val images = mutableListOf(
    R.drawable.iv_background_biginner_game1,
    R.drawable.iv_background_biginner_game2,
    R.drawable.iv_candy_background_game1,
    R.drawable.iv_candy_background_game2,
    R.drawable.iv_candy_background_game3,
    R.drawable.iv_candy_background_game4,
    R.drawable.iv_candy_background_game5,
    R.drawable.iv_background_lake_game1,
    R.drawable.iv_background_lake_game2
)