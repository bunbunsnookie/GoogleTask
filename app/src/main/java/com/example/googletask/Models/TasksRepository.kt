package com.example.googletask.Models

class TasksRepository (private val db: MainDb) {
    suspend fun update(task: Task) = db.getDao().updateTask(task)

    suspend fun delete(task: Task) = db.getDao().deleteTask(task)

    suspend fun insert(task: Task) = db.getDao().insertTask(task)

    fun getAllTasks() = db.getDao().getAllTasks()

    fun getAllSubtasks(taskId: Int) = db.getDao().getAllSubtasks(taskId)
}