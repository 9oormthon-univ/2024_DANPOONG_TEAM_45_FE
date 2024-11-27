package com.example.myapplication.presentation.ui.fragment.quest

data class QuestDto(
    var id: Int,
    var gameType: String,
    var gameName: String,
    var gameDescript: String,
    var gameImg: Int,
    var gameState: Int,
    val isCleared : Boolean,
    var isOpen : Boolean = false
)
