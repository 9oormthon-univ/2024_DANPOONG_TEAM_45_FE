package com.example.myapplication.presentation.ui.activity

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.DRAG_FLAG_GLOBAL
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.DragStartHelper
import androidx.draganddrop.DropHelper
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityGameBinding

interface GameInterface {
    // 게임 진행 관련 초기화 함수
    fun initViewModel()
    fun initBlock()
    fun initGame()
    fun initCharacter(gameId: Int, binding: ActivityGameBinding) {
        // 게임 아이템 좌표 초기화
        // 캐릭터 다시 원래 위치로
        val character = binding.ivGameCharacter
        val candy = binding.ivGameCandy
        val fire = binding.ivGameFire
        val fan = binding.ivGameFan

        when (gameId) {
            3 -> {
                character.translationX = 0f  // X 좌표 초기화
                character.translationY = 0f  // Y 좌표 초기화
                character.invalidate() // 강제로 뷰 갱신
            }
            4, 5 -> {
                character.translationX = -320f
                character.translationY = 0f

                candy.translationX = 200f
                candy.translationY = 0f
            }

            6 -> {
                character.translationX = -280f
                character.translationY = 0f

                candy.translationX = 200f
                candy.translationY = 0f
            }
            7 -> {
                character.translationX = -360f
                character.translationY = -180f

                fire.translationX = -580f
                fire.translationY = 180f

                candy.translationX = 280f
                candy.translationY = 0f

                fan.translationX = -540f
                fan.translationY = 0f
            }
        }
    }
    // drag 아이템 제거
    fun clearDragTargets(binding: ActivityGameBinding) {
        binding.linearLayoutBlockGameList.removeAllViews()
    }
    fun addBlock(blockDTO: BlockDTO) // drag 아이템 추가


    // drag and drop ----------------------------------------------
    // drag 시작
    fun setupDragSources(dragSources: List<View>) {
        dragSources.forEach { source ->
            source.setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val imageResId = dragSources.indexOf(source)
                    Log.d("Drag Event", "Starting drag with ID: $imageResId")

                    val dragClipData = ClipData.newPlainText("DragData", imageResId.toString())
                    val dragShadow = View.DragShadowBuilder(view)

                    view.startDragAndDrop(
                        dragClipData,
                        dragShadow,
                        null,
                        View.DRAG_FLAG_GLOBAL
                    )
                    true
                } else {
                    false
                }
            }
        }
    }

    // drop의 target id 찾기
    fun setupDropTargets(dropTargets: List<View>, context: Context) {
        val activity = context as? Activity ?: throw IllegalArgumentException("Context must be an Activity")

        dropTargets.forEach { target ->
            DropHelper.configureView(
                activity,
                target,
                arrayOf(MIMETYPE_TEXT_PLAIN),
                DropHelper.Options.Builder()
                    .setHighlightColor(getColor(context, R.color.water_color))
                    .build()
            ) { view, payload ->
                val item = payload.clip.getItemAt(0)
                val imageResId = item.text.toString().toIntOrNull()
                if (imageResId != null) {
                    val dropTargetId = dropTargets.indexOf(target)

//                    // 현재 타겟에 이미 블록이 놓여 있는 경우 - 다시 제자리에 갖다놓기
//                    val previousBlockId = targetBlockMap[dropTargetId]
//                    if (previousBlockId != null) {
//                        val previousBlock = dragSources[previousBlockId]
//                        val previousBlockDTO = previousBlock.tag as? BlockDTO
//                        val previousBlockType = previousBlockDTO?.blockType
//
//                        // 이전에 놓인 블록이 repeat이면 제외
//                        if (previousBlockType != getString(R.string.block_type_repeat)) {
//                            dragSources[previousBlockId].visibility = View.VISIBLE
//                            removeBlockFromMoveWay(previousBlockId)
//                        }
//                    }

                    handleImageDrop(view, imageResId, dropTargetId)
                } else {
                    Log.e(TAG, "Failed to retrieve imageResId from ClipData")
                }

                // 드롭 후 다른 데이터 처리
                payload.partition { it == item }.second
            }
        }
    }
    fun handleImageDrop(target: View, dragId: Int, dropId: Int)// drag와 drop 매핑

    // 기타 기능
    fun blockVisibility(visibleBlock: View, goneBlock: View) {
        visibleBlock.visibility = View.VISIBLE
        goneBlock.visibility = View.GONE
    } // 블록 visibility 변경
    fun Int.dpToPx(): Int // dp -> px 변환
}