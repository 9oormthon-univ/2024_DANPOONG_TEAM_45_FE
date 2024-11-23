package com.example.myapplication.domain.model

sealed class ChatMessage {
    data class RIGHT(val message: String) : ChatMessage()
    data class LEFT(val message: String) : ChatMessage()
}