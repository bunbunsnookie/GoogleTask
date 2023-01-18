package com.example.googletask.ViewModels

import androidx.lifecycle.ViewModel
import com.example.googletask.Models.Task
import com.example.googletask.Models.TasksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class MainActivityViewModel(private val repository: TasksRepository): ViewModel() {

    fun update(task: Task){
        viewModelScope.launch {
            repository.update(task)
        }

    }

    fun delete(task: Task){
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    fun insert(task: Task){
        viewModelScope.launch {
            repository.insert(task)
        }
    }

    fun getAllTask() = repository.getAllTasks()

    fun getAllSubtasks(taskId: Int) = repository.getAllSubtasks(taskId)
}
