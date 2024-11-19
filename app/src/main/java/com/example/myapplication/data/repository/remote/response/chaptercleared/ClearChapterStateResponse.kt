package com.example.myapplication.data.repository.remote.response.chaptercleared

data class ClearChapterStateResponse(
    val id : Int,
    val userId : Int,
    val chapterId : Int,
    val isCleared : Boolean
)
