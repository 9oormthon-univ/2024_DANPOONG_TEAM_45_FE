package com.example.myapplication.presentation.ui.state

sealed class UiState<out T>(val status: Status) {
    data object Loading : UiState<Nothing>(Status.LOADING)
    data class Success<T>(val data: T) : UiState<T>(Status.SUCCESS)
    data object Failed : UiState<Nothing>(Status.FAILED)
}

enum class Status {
    LOADING, SUCCESS, FAILED
}