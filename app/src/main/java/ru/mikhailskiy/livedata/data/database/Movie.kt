package ru.mikhailskiy.livedata.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "movieId")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String?
)