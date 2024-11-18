package com.example.myapplication.presentation.ui.fragment.quest

sealed class IslandDto {
    data class IslandRight(val background: Int, val island : Int, val locked : Boolean, val name : String) : IslandDto()
    data class IslandLeft(val background: Int, val island : Int, val locked : Boolean, val name : String) : IslandDto()
}