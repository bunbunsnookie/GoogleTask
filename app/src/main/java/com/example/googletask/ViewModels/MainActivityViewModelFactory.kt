package com.example.googletask.ViewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googletask.Models.TasksRepository

@Suppress("UNCHECKED_CAST")
class MainActivityViewModelFactory (application: Application, private val repository: TasksRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(repository) as T
    }
}