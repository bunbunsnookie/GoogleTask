package com.example.googletask.Models

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.googletask.R
import com.example.googletask.ViewModels.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar

class TaskTouchHelperCallback(
    private val viewModel: MainActivityViewModel,
    private val adapter: TasksAdapter,
    private val view: View
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val task = adapter.tasks[viewHolder.adapterPosition]
        viewModel.delete(task)
        Snackbar.make(view, R.string.task_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {viewModel.insert(task)}
            .show()
    }
}