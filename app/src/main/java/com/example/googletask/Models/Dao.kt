package com.example.googletask.Models
import androidx.room.*
import androidx.room.Dao
import com.example.googletask.Models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<Task>

    @Insert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM tasks WHERE fromTask = :taskId")
    fun getAllSubtasks(taskId: Int): List<Task>

}