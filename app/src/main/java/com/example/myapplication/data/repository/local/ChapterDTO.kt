package com.example.myapplication.data.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChapterDTO(
    @PrimaryKey(autoGenerate = true) val cid: Int = 0,
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "isCleared") val isCleared: Boolean = false,
    @ColumnInfo(name = "isRewardButtonActive") val isRewardButtonActive: Boolean = true
)