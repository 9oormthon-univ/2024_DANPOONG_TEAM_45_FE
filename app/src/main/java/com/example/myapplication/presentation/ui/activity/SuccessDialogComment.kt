package com.example.myapplication.presentation.ui.activity

enum class SuccessDialogComment(val title: String, val subTitle: String) {
    SWEET("너무 달콤하다!", "사탕을 먹을 수 있게 도와줘서 고마워 :)"),
    CANDY("우와 또 사탕이다!", "너무 맛있다 ㅎㅎ  도와줘서 고마워 :)"),
    GUM("휴~ 무사히 껌을 피했어!", "도와줘서 고마워 :)"),
    FIRE("우와 불이 무사히 꺼졌어!", "도와줘서 고마워 :)"),
    FIRE2("우와 불이 무사히 꺼졌어!", "도와줘서 고마워 :)");

    companion object {
        fun getAllComments(): List<Pair<String, String>> {
            return values().map { it.title to it.subTitle }
        }
    }
}