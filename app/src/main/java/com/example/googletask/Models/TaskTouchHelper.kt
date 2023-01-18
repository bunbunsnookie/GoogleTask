package com.example.googletask.Models

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.googletask.ViewModels.MainActivityViewModel

class TaskTouchHelper(
    viewModel: MainActivityViewModel,
    adapter: TasksAdapter,
    view: View
) : ItemTouchHelper(TaskTouchHelperCallback(viewModel, adapter, view))