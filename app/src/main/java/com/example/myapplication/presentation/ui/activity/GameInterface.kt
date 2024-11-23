package com.example.myapplication.presentation.ui.activity

import android.view.View
import android.widget.ImageButton

interface GameInterface {
    // 게임 진행 관련 초기화 함수
    fun initViewModel()
    fun initBlock()
    fun initGame()
    fun initCharacter(gameId: Int) // 게임 아이템 좌표 초기화
    fun clearBlocks() // drag 아이템 제거
    fun addBlock(blockDTO: BlockDTO) // drag 아이템 추가
    fun gameFunction() // 각종 버튼들 처리, repeat 블록 있으면 moveWay에 추가

    // drag and drop
    fun setupDragSources() // drag 시작
    fun setupDropTargets() // drop의 target id 찾기
    fun handleImageDrop(target: View, dragId: Int, dropId: Int) // drag와 drop 매핑

    // 게임 성공 여부 판단
    fun checkSuccess() // 게임 성공인지 판별
    fun showSuccessDialog(exit: Boolean) // 게임 성공 시 성공 다이얼로그 출력, exit : 나가기 버튼 눌렀을 때 다이얼로그
    fun showFailDialog() // 게임 실패 시 실패 다이얼로그 출력

    // 캐릭터 이동
    fun characterMove()
    fun backgroundVisibility(background: Int)
    fun isFireCondition(): Boolean // 불 꺼지는 상황인지 체크
    fun handleFireCondition() // 불 끄기

    // 기타 기능
    fun blockVisibility(visibleBlock: View, goneBlock: View) // 블록 visibility 변경
    fun Int.dpToPx(): Int // dp -> px 변환
}