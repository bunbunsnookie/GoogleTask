package com.example.googletask.Models

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googletask.R
import com.example.googletask.databinding.TaskBinding
import com.example.googletask.ViewModels.MainActivityViewModel

class TasksAdapter(
    var tasks: List<Task>,
    private val viewModel: MainActivityViewModel,
    val clickListener: (Task) -> Unit,
) :
    RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    inner class TasksViewHolder(
        val binding: TaskBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.recyclerItem.setOnClickListener { clickListener(tasks[adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TasksAdapter.TasksViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TaskBinding.inflate(layoutInflater, parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksAdapter.TasksViewHolder, position: Int) {
        holder.binding.apply {
            edTitle.text = tasks[position].title
            edDesc.text = tasks[position].description

            edTitle.apply {
                paintFlags =
                    if (tasks[position].completed) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            edDesc.apply {
                paintFlags =
                    if (tasks[position].completed) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            edCompleted.setOnCheckedChangeListener(null)
            edCompleted.isChecked = tasks[position].completed
            edCompleted.setOnCheckedChangeListener { _, isChecked ->
                val task = tasks[position]
                val editedTask =
                    Task(task.title, task.description, task.data, task.chosen, isChecked, task.fromTask, task.id)
                viewModel.update(editedTask)
            }
            edChosen.setOnClickListener(null)
            val isChosen = tasks[position].chosen
            edChosen.setImageResource(getStarDrawable(isChosen))
            edChosen.setOnClickListener {
                val task = tasks[position]
                val editedTask = Task(
                    task.title,
                    task.description,
                    task.data,
                    !task.chosen,
                    task.completed,
                    task.fromTask,
                    task.id
                )
                viewModel.update(editedTask)
            }
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun getStarDrawable(isImportant: Boolean): Int {
        return when(isImportant) {
            true -> R.drawable.ic_star
            false -> R.drawable.ic_star_outline
        }
    }
}