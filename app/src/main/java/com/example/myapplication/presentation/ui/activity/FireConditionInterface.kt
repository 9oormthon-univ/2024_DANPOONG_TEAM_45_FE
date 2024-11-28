package com.example.myapplication.presentation.ui.activity

import android.widget.ImageView
import com.example.myapplication.R

interface FireConditionInterface {
    fun isFireCondition(binding: GameActivity, gameId: Int, move: List<Int>): Boolean {
        return when (gameId) {
            6 -> isFireConditionForGame6(move)
            else -> isFireConditionForOtherGames(move)
        }
    }

    private fun isFireConditionForGame6(move: List<Int>): Boolean {
        val fanBlockOrder = listOf(
            R.string.game_move_straight,
            R.string.game_move_up,
            R.string.game_move_straight,
            R.string.game_fanning
        )

        return if (move.size >= 4) {
            move.take(4) == fanBlockOrder
        } else {
            false
        }
    }

    private fun isFireConditionForOtherGames(move: List<Int>): Boolean {
        return R.string.game_fanning == move.getOrNull(0)
    }

    fun handleFireCondition(binding: GameActivity) {
        // Fire 처리 로직
        val gameFan = binding.findViewById<ImageView>(R.id.iv_game_fan)
        val gameFire = binding.findViewById<ImageView>(R.id.iv_game_fire)
        binding.blockVisibility(gameFan, gameFire)
    }
}