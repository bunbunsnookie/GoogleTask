package com.example.googletask.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "tasks")
data class Task (
        @ColumnInfo(name = "title")
        var title: String,
        @ColumnInfo(name = "description")
        var description: String,
        @ColumnInfo(name = "data")
        var data: String,
        @ColumnInfo(name = "chosen")
        var chosen: Boolean,
        @ColumnInfo(name = "completed")
        var completed: Boolean,
        @ColumnInfo(name = "fromTask", defaultValue = "null")
        var fromTask: Int?,
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null,
        )


