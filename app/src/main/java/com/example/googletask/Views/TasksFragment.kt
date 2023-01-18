package com.example.googletask.Views

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googletask.Models.*
import com.example.googletask.R
import com.example.googletask.databinding.FragmentTasksBinding
import com.example.googletask.ViewModels.MainActivityViewModel
import com.example.googletask.ViewModels.MainActivityViewModelFactory
import com.google.android.material.snackbar.Snackbar

class TasksFragment : Fragment() {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var adapter: TasksAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var activityContext: Context
    private lateinit var application: Application
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        val database = MainDb.getInstance(activityContext)
        val repository = TasksRepository(database)
        val viewModelFactory = MainActivityViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]

        adapter =
            TasksAdapter(viewModel.getAllTask().value?.reversed() ?: listOf(), viewModel) {
                openEditTaskActivity(it)
            }
        layoutManager = LinearLayoutManager(activityContext)
        val tasksObserver = Observer<List<Task>> {
            adapter.tasks = it.reversed()
            adapter.notifyDataSetChanged()
        }
        viewModel.getAllTask().observe(viewLifecycleOwner, tasksObserver)

        val itemTouchHelper = TaskTouchHelper(viewModel, adapter, binding.root)
        itemTouchHelper.attachToRecyclerView(binding.rvTasks)
        binding.apply {
            rvTasks.adapter = adapter
            rvTasks.layoutManager = layoutManager
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.findViewById<ImageButton>(R.id.Options)?.setOnClickListener {
            showPopup(it)
        }
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
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
        application = (context as Activity).application
    }

    private fun openEditTaskActivity(task: Task) {
        Intent(requireActivity(), TaskActivity::class.java).also { intent ->
            intent.putExtra(Const.TASK_EXTRA, TaskSerializer.fromTask(task))
            resultLauncher.launch(intent)
        }
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(activityContext, v)
        popup.inflate(R.menu.bottom_menu)
        popup.setOnMenuItemClickListener {menuItemClickListener(it)}
        popup.show()
    }

    @SuppressLint("NotifyDataSetChanged")
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