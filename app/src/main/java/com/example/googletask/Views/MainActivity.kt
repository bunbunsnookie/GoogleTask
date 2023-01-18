package com.example.googletask.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.googletask.Models.MainDb
import com.example.googletask.Models.Task
import com.example.googletask.Models.TasksRepository
import com.example.googletask.ViewModels.MainActivityViewModel
import com.example.googletask.ViewModels.MainActivityViewModelFactory
import com.example.googletask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AddTaskDialog.CreateTaskDialogInterface {
    lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = MainDb.getInstance(this)
        val repository = TasksRepository(database)
        val factory = MainActivityViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]

        binding.apply {
            fab.setOnClickListener{
                AddTaskDialog().show(supportFragmentManager, "Add task")
            }
        }
    }

    override fun addTask(title: String, desc: String, date: String, chosen: Boolean, completed: Boolean, fromTask : Int?) {
        val newTask = Task(title, desc, date, chosen, completed, fromTask)
        viewModel.insert(newTask)
    }
}