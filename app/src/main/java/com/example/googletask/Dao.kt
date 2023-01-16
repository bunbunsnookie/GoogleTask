package com.example.googletask
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertTask(task: Task)
    @Query("SELECT * FROM tasks")
    fun getAllTask(): Flow<List<Task>>
}