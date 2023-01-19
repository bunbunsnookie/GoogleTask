package com.example.googletask.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googletask.Models.*
import com.example.googletask.R
import com.example.googletask.ViewModels.MainActivityViewModel
import com.example.googletask.ViewModels.MainActivityViewModelFactory
import com.example.googletask.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

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


        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Const.EDIT_TASK_RESULT_CODE) {
                val data: Intent = result.data ?: return@registerForActivityResult
                val task = TaskSerializer.toTask(data.getSerializableExtra(Const.TASK_EXTRA) as TaskSerializer)
                viewModel.delete(task)
                Snackbar.make(binding.root, R.string.task_deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) {
                        viewModel.insert(task)
                    }
                    .show()
            }
        }
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
            Options.setOnClickListener { showPopup(Options) }
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

    private fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        popup.inflate(R.menu.options_menu)
        popup.setOnMenuItemClickListener {menuItemClickListener(it)}
        popup.show()
    }

    private fun menuItemClickListener(it: MenuItem): Boolean {
        return when(it.itemId) {
            R.id.miSortByName -> {
                adapter.tasks = adapter.tasks.sortedBy { task -> task.title }
                adapter.notifyDataSetChanged()
                true
            }
            R.id.miSortByDate -> {
                adapter.tasks = adapter.tasks.sortedBy { task -> task.data}
                adapter.notifyDataSetChanged()
                true
            }
            R.id.miSortByImportance -> {
                adapter.tasks = adapter.tasks.sortedBy { task -> !task.chosen }
                adapter.notifyDataSetChanged()
                true
            }
            else -> false
        }
    }
}