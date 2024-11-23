package com.example.myapplication.presentation.ui.activity

import android.view.View
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityGameBinding

enum class GameDropTarget(val viewId: Int) {
    SPACE1(R.id.ib_biginner_game1_space1),
    SPACE2(R.id.ib_biginner_game1_space2),
    SPACE3(R.id.ib_biginner_game1_space3),
    SPACE4(R.id.ib_biginner_game1_space4),
    SPACE5(R.id.ib_biginner_game1_space5),
    SPACE6(R.id.ib_biginner_game1_space6),
    SPACE7(R.id.ib_biginner_game1_space7);

    companion object {
        // 모든 `viewId`를 반환 (뷰 초기화에 사용 가능)
        fun getAllViews(binding: ActivityGameBinding): List<View> {
            return values().map { target ->
                binding.root.findViewById<View>(target.viewId)
            }
        }
    }
}

