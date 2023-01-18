package com.example.googletask.Views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googletask.Models.*
import com.example.googletask.ViewModels.MainActivityViewModel
import com.example.googletask.ViewModels.MainActivityViewModelFactory
import com.example.googletask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AddTaskDialog.CreateTaskDialogInterface {
    lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TasksAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = MainDb.getInstance(this)
        val repository = TasksRepository(database)
        val factory = MainActivityViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]

        adapter =
            TasksAdapter(viewModel.getAllTask().value?.reversed() ?: listOf(), viewModel) {
                openEditTaskActivity(it)
            }
        layoutManager = LinearLayoutManager(this)
        val tasksObserver = Observer<List<Task>> {
            adapter.tasks = it.reversed()
            adapter.notifyDataSetChanged()
        }
        viewModel.getAllTask().observe(this, tasksObserver)

        binding.apply {
            fab.setOnClickListener{
                AddTaskDialog().show(supportFragmentManager, "Add task")
            }
            edRecyclerTasks.adapter = adapter
            edRecyclerTasks.layoutManager = layoutManager
        }
    }

    private fun openEditTaskActivity(task: Task) {
        Intent(this, TaskActivity::class.java).also { intent ->
            intent.putExtra(Const.TASK_EXTRA, TaskSerializer.fromTask(task))
            resultLauncher.launch(intent)
        }
    }

    override fun addTask(title: String, desc: String, date: String, chosen: Boolean, completed: Boolean, fromTask : Int?) {
        val newTask = Task(title, desc, date, chosen, completed, fromTask)
        viewModel.insert(newTask)
    }
}