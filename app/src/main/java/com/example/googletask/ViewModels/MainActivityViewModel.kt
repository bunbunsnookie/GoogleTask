package com.example.googletask.ViewModels

import androidx.lifecycle.ViewModel
import com.example.googletask.Models.Task
import com.example.googletask.Models.TasksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: TasksRepository): ViewModel() {

    fun update(task: Task) = CoroutineScope(Dispatchers.Main).launch {
        repository.update(task)
    }

    fun delete(task: Task) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(task)
    }

    fun insert(task: Task) = CoroutineScope(Dispatchers.Main).launch {
        repository.insert(task)
    }

    fun getAllTask() = repository.getAllTasks()

    fun getAllSubtasks(taskId: Int) = repository.getAllSubtasks(taskId)
}
