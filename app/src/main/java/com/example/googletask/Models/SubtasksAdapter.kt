package com.example.googletask.Models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googletask.databinding.SubtaskBinding
import com.example.googletask.ViewModels.MainActivityViewModel

class SubtasksAdapter(
    var subtasks: List<Task>,
    private val viewModel: MainActivityViewModel
) : RecyclerView.Adapter<SubtasksAdapter.SubtasksListViewHolder>() {

    inner class SubtasksListViewHolder(
        val binding: SubtaskBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubtasksAdapter.SubtasksListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SubtaskBinding.inflate(layoutInflater, parent, false)
        return SubtasksListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubtasksListViewHolder, position: Int) {
        holder.binding.apply {
            edTitle.setText(subtasks[position].title)
            btnSave.setOnClickListener {
                val subtask = subtasks[holder.adapterPosition]
                viewModel.update(Task(edTitle.text.toString(), subtask.description, subtask.data, subtask.chosen, subtask.completed, subtask.fromTask, subtask.id))
            }
            cbCompletedSub.setOnCheckedChangeListener(null)
            cbCompletedSub.isChecked = false
            cbCompletedSub.setOnCheckedChangeListener{_, isChecked ->
                if (isChecked) {
                    viewModel.delete(subtasks[holder.adapterPosition])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return subtasks.size
    }
}