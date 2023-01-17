package com.example.googletask

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "tasks")
data class Task (
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null,
        @ColumnInfo(name = "title")
        var title: String,
        @ColumnInfo(name = "description")
        var description: String,
        @ColumnInfo(name = "data")
        var data: String,
        @ColumnInfo(name = "chosen")
        var chosen: Boolean,
        @ColumnInfo(name = "completed")
        var complited: Boolean,
        @ColumnInfo(name = "fromTask")
        var fromTask: Int? = null,
        )


