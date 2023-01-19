package com.example.googletask.Models
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
@Dao
interface Dao {

    @Query("SELECT * FROM tasks WHERE fromTask is null")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM tasks WHERE fromTask = :taskId")
    fun getAllSubtasks(taskId: Int): LiveData<List<Task>>

}